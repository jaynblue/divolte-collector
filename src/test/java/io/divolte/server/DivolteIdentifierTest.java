/*
 * Copyright 2017 GoDataDriven B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.divolte.server;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;

public class DivolteIdentifierTest {
    @Test
    public void divolteIdentifiersShouldBeUnique() {
        final int num = 100000;
        final Set<String> values = new HashSet<>(num + num / 2);
        for (int c = 0; c < num; c++) {
            values.add(DivolteIdentifier.generate().value);
        }

        assertEquals(num, values.size());
    }

    @Test
    public void divolteIdentifiersShouldEncodeTimestamp() {
        DivolteIdentifier cv = DivolteIdentifier.generate(42);
        assertEquals(Optional.of(42L), DivolteIdentifier.tryParse(cv.value).map(di -> di.timestamp));
    }

    @Test
    public void equalCookieValuesShouldBeConsistentWithHashcodeAndEquals() {
        DivolteIdentifier left = DivolteIdentifier.generate();
        Optional<DivolteIdentifier> right = DivolteIdentifier.tryParse(left.value);

        assertEquals(Optional.of(left), right);
        assertEquals(Optional.of(left.hashCode()), right.map(DivolteIdentifier::hashCode));

        assertNotEquals(DivolteIdentifier.generate(42), DivolteIdentifier.generate(42));
    }

    @Test
    public void divolteIdentifiersShouldParseVersionAndTimestamp() {
        String stringValue = "0:16:5mRCeUO4p2_6R7u1m9ZoxXG2AfBeJeHD";
        Optional<DivolteIdentifier> value = DivolteIdentifier.tryParse(stringValue);
        assertEquals(Optional.of(42L), value.map(v -> v.timestamp));
        assertEquals(Optional.of('0'), value.map(v -> v.version));
        assertEquals(Optional.of(stringValue), value.map(v -> v.value));
    }
}
