package com.frenderman.scarecrows.misc;

import javax.annotation.Nullable;

/**
 * Do you need a null? We got you covered!
 */
public class HereHaveANullForFree {

    private final Object nullObject;

    public HereHaveANullForFree(@Nullable Object nullObject) {
        if (nullObject != null) {
            throw new IllegalArgumentException("No");
        }
        this.nullObject = null;
    }

    @Nullable
    public Object getNull() {
        return this.nullObject;
    }
}
