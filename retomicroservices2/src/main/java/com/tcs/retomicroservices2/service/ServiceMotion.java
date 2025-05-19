package com.tcs.retomicroservices2.service;

import com.tcs.retomicroservices2.entity.Motion;
import java.util.List;

public interface ServiceMotion {
    Motion postMotion(Motion motion);
    List<Motion> getMotions();
    Motion getMotionById(long idMotion);
    void putMotion(long idMotion, Motion updatedMotion);
    void deleteMotion(long idMotion);
    List<Motion> getMotionsByAccountId(Long accountId);
    Double getCurrentBalance(Long accountId);
}