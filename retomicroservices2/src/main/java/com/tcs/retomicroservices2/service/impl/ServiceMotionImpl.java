package com.tcs.retomicroservices2.service.impl;

import com.tcs.retomicroservices2.entity.Account;
import com.tcs.retomicroservices2.entity.Motion;
import com.tcs.retomicroservices2.repository.MotionRepository;
import com.tcs.retomicroservices2.service.ServiceAccount;
import com.tcs.retomicroservices2.service.ServiceMotion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceMotionImpl implements ServiceMotion {

    @Autowired
    private MotionRepository motionRepository;

    @Autowired
    private ServiceAccount serviceAccount;

    @Override
    public Motion postMotion(Motion motion) {
        // Verificar que la cuenta existe
        Account account = serviceAccount.getAccountById(motion.getAccountId());

        // Establecer fecha actual si no se proporciona
        if (motion.getDatemotion() == null) {
            motion.setDatemotion(LocalDateTime.now());
        }

        // Calcular el saldo actual
        Double currentBalance = getCurrentBalance(motion.getAccountId());

        // Calcular nuevo saldo según el tipo de movimiento
        if ("RETIRO".equalsIgnoreCase(motion.getTypemotion())) {
            if (motion.getValue() > currentBalance) {
                throw new RuntimeException("Saldo insuficiente para realizar este retiro");
            }
            motion.setMotionbalance(currentBalance - motion.getValue());
        } else if ("DEPOSITO".equalsIgnoreCase(motion.getTypemotion())) {
            motion.setMotionbalance(currentBalance + motion.getValue());
        } else {
            throw new RuntimeException("Tipo de movimiento no válido. Debe ser DEPOSITO o RETIRO");
        }

        // Guardar el movimiento
        Motion savedMotion = motionRepository.save(motion);

        // Poblar los datos de la cuenta antes de retornar
        return populateAccountData(savedMotion);
    }

    @Override
    public List<Motion> getMotions() {
        return motionRepository.findAll().stream()
                .map(this::populateAccountData)
                .collect(Collectors.toList());
    }

    @Override
    public Motion getMotionById(long idMotion) {
        Motion motion = motionRepository.findById(idMotion)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));
        return populateAccountData(motion);
    }

    @Override
    public void putMotion(long idMotion, Motion updatedMotion) {
        Motion existing = motionRepository.findById(idMotion)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));

        // No permitir modificar la cuenta asociada
        if (updatedMotion.getAccountId() != null && !existing.getAccountId().equals(updatedMotion.getAccountId())) {
            throw new RuntimeException("No se puede cambiar la cuenta asociada a un movimiento");
        }

        // Actualizar campos
        if (updatedMotion.getDatemotion() != null) {
            existing.setDatemotion(updatedMotion.getDatemotion());
        }
        if (updatedMotion.getTypemotion() != null) {
            existing.setTypemotion(updatedMotion.getTypemotion());
        }
        if (updatedMotion.getValue() != null) {
            existing.setValue(updatedMotion.getValue());
        }

        // Recalcular el saldo y actualizar movimientos posteriores
        recalculateBalances(existing.getAccountId());

        // Guardar cambios
        motionRepository.save(existing);
    }

    @Override
    public void deleteMotion(long idMotion) {
        Motion motion = motionRepository.findById(idMotion)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));

        Long accountId = motion.getAccountId();

        motionRepository.deleteById(idMotion);

        // Recalcular saldos de todos los movimientos
        recalculateBalances(accountId);
    }

    @Override
    public List<Motion> getMotionsByAccountId(Long accountId) {
        // Verificar que la cuenta existe
        serviceAccount.getAccountById(accountId);

        return motionRepository.findByAccountIdOrderByDatemotionDesc(accountId).stream()
                .map(this::populateAccountData)
                .collect(Collectors.toList());
    }

    @Override
    public Double getCurrentBalance(Long accountId) {
        // Verificar que la cuenta existe
        Account account = serviceAccount.getAccountById(accountId);

        // Obtener saldo inicial de la cuenta
        Double initialBalance = Double.parseDouble(account.getInibalance());

        // Obtener todos los movimientos ordenados por fecha descendente
        List<Motion> motions = motionRepository.findByAccountIdOrderByDatemotionDesc(accountId);

        if (motions.isEmpty()) {
            return initialBalance;
        }

        // El primer movimiento (el más reciente) tendrá el saldo actual
        return motions.get(0).getMotionbalance();
    }

    /**
     * Recalcula los saldos de todos los movimientos de una cuenta
     */
    private void recalculateBalances(Long accountId) {
        // Obtener saldo inicial
        Account account = serviceAccount.getAccountById(accountId);
        Double balance = Double.parseDouble(account.getInibalance());

        // Obtener todos los movimientos ordenados por fecha
        List<Motion> allMotions = motionRepository.findByAccountId(accountId).stream()
                .sorted((m1, m2) -> m1.getDatemotion().compareTo(m2.getDatemotion()))
                .collect(Collectors.toList());

        // Recalcular saldos para todos los movimientos
        for (Motion motion : allMotions) {
            if ("DEPOSITO".equalsIgnoreCase(motion.getTypemotion())) {
                balance += motion.getValue();
            } else if ("RETIRO".equalsIgnoreCase(motion.getTypemotion())) {
                balance -= motion.getValue();
            }

            // Actualizar saldo del movimiento
            motion.setMotionbalance(balance);
            motionRepository.save(motion);
        }
    }

    /**
     * Método para obtener datos completos de la cuenta usando ServiceAccount
     */
    private Motion populateAccountData(Motion motion) {
        if (motion.getAccountId() != null) {
            try {
                Account account = serviceAccount.getAccountById(motion.getAccountId());
                motion.setAccount(account);
            } catch (Exception e) {
                System.out.println("Error al obtener cuenta: " + e.getMessage());
            }
        }
        return motion;
    }
}


