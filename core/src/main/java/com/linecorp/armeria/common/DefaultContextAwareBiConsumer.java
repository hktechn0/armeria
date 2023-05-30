/*
 * Copyright 2023 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.linecorp.armeria.common;

import static java.util.Objects.requireNonNull;

import java.util.function.BiConsumer;

import com.linecorp.armeria.common.util.SafeCloseable;

final class DefaultContextAwareBiConsumer<T, U> implements ContextAwareBiConsumer<T, U> {
    private final RequestContext context;
    private final BiConsumer<T, U> action;

    DefaultContextAwareBiConsumer(RequestContext context, BiConsumer<T, U> action) {
        this.context = requireNonNull(context, "context");
        this.action = requireNonNull(action, "action");
    }

    @Override
    public RequestContext context() {
        return context;
    }

    @Override
    public BiConsumer<T, U> withoutContext() {
        return action;
    }

    @Override
    public void accept(T t, U u) {
        try (SafeCloseable ignored = context.push()) {
            action.accept(t, u);
        }
    }
}
