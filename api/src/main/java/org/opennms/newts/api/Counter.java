/*
 * Copyright 2014, The OpenNMS Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opennms.newts.api;


import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.primitives.UnsignedLong;


public class Counter extends ValueType<UnsignedLong> {
    
    private static final long serialVersionUID = 1L;
    private static final UnsignedLong MAX32 = toUnsignedLong(0xFFFFFFFFL);
    private static final UnsignedLong MAX64 = toUnsignedLong(0xFFFFFFFFFFFFFFFFL);

    private final UnsignedLong m_value;

    public Counter(long value) {
        this(UnsignedLong.fromLongBits(value));
    }

    public Counter(UnsignedLong value) {
        m_value = checkNotNull(value, "value");
    }

    @Override
    public Counter plus(Number value) {
        return new Counter(getValue().plus(toUnsignedLong(value)));
    }

    @Override
    public Counter minus(Number value) {
        return new Counter(getValue().minus(toUnsignedLong(value)));
    }

    @Override
    public ValueType<UnsignedLong> delta(Number value) {
        Long difference = longValue() - value.longValue();
        if (difference < 0) {
            Long difference32 = difference + 0xFFFFFFFFL + 1;
            if (difference32 < 0) {
                return new Counter(toUnsignedLong(difference +  0xFFFFFFFFFFFFFFFFL + 1));
            }
            else {
                return new Counter(toUnsignedLong(difference32));
            }
        }
        else {
            return new Counter(toUnsignedLong(difference));
        }

    }

    private boolean isNegative(UnsignedLong val) {
        return val.compareTo(UnsignedLong.ZERO) < 0;
    }

    @Override
    public Counter times(Number value) {
        return new Counter(getValue().times(toUnsignedLong(value)));
    }

    @Override
    public Counter divideBy(Number value) {
        return new Counter(getValue().dividedBy(toUnsignedLong(value)));
    }

    @Override
    UnsignedLong getValue() {
        return m_value;
    }

    @Override
    public MetricType getType() {
        return MetricType.COUNTER;
    }

    @Override
    public int intValue() {
        return getValue().intValue();
    }

    @Override
    public long longValue() {
        return getValue().longValue();
    }

    @Override
    public float floatValue() {
        return getValue().floatValue();
    }

    @Override
    public double doubleValue() {
        return getValue().doubleValue();
    }

    protected static UnsignedLong toUnsignedLong(Number value) {
        if (value instanceof UnsignedLong) return (UnsignedLong) value;
        return UnsignedLong.fromLongBits(value.longValue());
    }

}
