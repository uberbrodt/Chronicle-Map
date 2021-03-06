/*
 * Copyright 2014 Higher Frequency Trading
 *
 * http://www.higherfrequencytrading.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.openhft.chronicle.hash.serialization.internal;

import net.openhft.chronicle.hash.serialization.SizeMarshaller;
import net.openhft.lang.io.Bytes;

import static net.openhft.lang.io.IOTools.stopBitLength;

public final class SizeMarshallers {

    public static SizeMarshaller stopBit() {
        return StopBit.INSTANCE;
    }

    private enum StopBit implements SizeMarshaller {
        INSTANCE;

        @Override
        public int sizeEncodingSize(long size) {
            return stopBitLength(size);
        }

        @Override
        public long minEncodableSize() {
            return 0L;
        }

        @Override
        public int minSizeEncodingSize() {
            return 1;
        }

        @Override
        public int maxSizeEncodingSize() {
            return 9;
        }

        @Override
        public void writeSize(Bytes bytes, long size) {
            bytes.writeStopBit(size);
        }

        @Override
        public long readSize(Bytes bytes) {
            return bytes.readStopBit();
        }
    }

    public static SizeMarshaller constant(long size) {
        return new ConstantSizeMarshaller(size);
    }

    private static class ConstantSizeMarshaller implements SizeMarshaller {
        private static final long serialVersionUID = 0L;

        private final long size;

        private ConstantSizeMarshaller(long size) {
            this.size = size;
        }

        @Override
        public int sizeEncodingSize(long size) {
            return 0;
        }

        @Override
        public long minEncodableSize() {
            return size;
        }

        @Override
        public int minSizeEncodingSize() {
            return 0;
        }

        @Override
        public int maxSizeEncodingSize() {
            return 0;
        }

        @Override
        public void writeSize(Bytes bytes, long size) {
            assert size == this.size;
            // do nothing
        }

        @Override
        public long readSize(Bytes bytes) {
            return size;
        }
    }

    private SizeMarshallers() {}
}
