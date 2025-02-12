/*
 * Copyright (c) 2019, Xiaomi, Inc.  All rights reserved.
 * This source code is licensed under the Apache License Version 2.0, which
 * can be found in the LICENSE file in the root directory of this source tree.
 */
package com.xiaomi.thain.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.xiaomi.thain.common.exception.ThainRuntimeException;
import com.xiaomi.thain.server.dao.X5Dao;
import com.xiaomi.thain.server.model.X5Config;
import com.xiaomi.thain.server.model.dp.X5ConfigDp;
import com.xiaomi.thain.server.model.dr.X5ConfigDr;
import com.xiaomi.thain.server.model.rq.X5ConfigRq;
import com.xiaomi.thain.server.service.X5Service;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liangyongrui@xiaomi.com
 * @date 19-5-7 上午11:01
 */
@Log4j2
@Service
public class X5ServiceImpl implements X5Service {

    @NonNull
    private final X5Dao x5Dao;

    public X5ServiceImpl(@NonNull X5Dao x5Dao) {
        this.x5Dao = x5Dao;
    }

    @Override
    public X5Config getX5Config(@NonNull String appId) {
        return x5Dao.getX5Config(appId).orElseThrow(() -> new ThainRuntimeException("AppId is not Existed"));
    }

    @Override
    public List<X5ConfigDr> getAllConfigs() {
        return x5Dao.getAllX5Config();
    }

    @Override
    public void deleteX5Config(@NonNull String appId) {
        x5Dao.deleteX5Config(appId);
    }

    @Override
    public boolean insertX5Config(@NonNull X5ConfigRq x5ConfigRq) {
        if (x5ConfigRq.principals.isEmpty()) {
            return false;
        }
        if (!x5Dao.getX5Config(x5ConfigRq.appId).isPresent()) {
            x5Dao.addX5Config(X5ConfigDp.builder()
                    .appId(x5ConfigRq.appId)
                    .appKey(x5ConfigRq.appKey)
                    .appName(x5ConfigRq.appName)
                    .description(x5ConfigRq.description)
                    .principal(JSON.toJSONString(x5ConfigRq.principals))
                    .build());
            return true;
        }
        return false;
    }

    @Override
    public boolean updateX5Config(@NonNull X5ConfigRq x5ConfigRq) {
        if (x5Dao.getX5Config(x5ConfigRq.appId).isPresent()) {
            x5Dao.updateX5Config(X5ConfigDp.builder()
                    .appId(x5ConfigRq.appId)
                    .appKey(x5ConfigRq.appKey)
                    .appName(x5ConfigRq.appName)
                    .description(x5ConfigRq.description)
                    .principal(JSON.toJSONString(x5ConfigRq.principals))
                    .build());
            return true;
        }
        return false;
    }
}

