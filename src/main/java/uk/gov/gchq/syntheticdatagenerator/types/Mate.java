/*
 * Copyright 2018-2021 Crown Copyright
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

package uk.gov.gchq.syntheticdatagenerator.types;

import java.util.Arrays;
import java.util.Random;
import java.util.StringJoiner;

import static java.util.Objects.requireNonNull;

public class Mate {
    private String uid;
    private Mate[] mates;
    private String mateType;

    public static Mate[] generateMany(final Random random, final int chain) {
        return new Mate[]{
                generateRecursive(random, chain, "Companiero Interino"),
                generateRecursive(random, chain, "Companiero Fijo"),
                generateRecursive(random, chain, "Companiero Titular")
        };
    }


    public static Mate generateRecursive(final Random random, final int chain, final String mateType) {
        Mate mate = Mate.generate(random, mateType);
        if (chain <= 1) {
            mate.setMate(null);
        } else {
            mate.setMate(Mate.generateMany(random, chain - 1));
        }
        return mate;
    }

    public static Mate generate(final Random random, final String mateType) {
        Mate Mate = new Mate();
        Mate.setUid(Pas.generateUID(random));
        Mate.setmateType(mateType);

        return Mate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(final String uid) {
        requireNonNull(uid);
        this.uid = uid;
    }

    public String getmateType() {
        return mateType;
    }

    public void setmateType(final String mateType) {
        requireNonNull(mateType);
        this.mateType = mateType;
    }

    public Mate[] getMate() {
        if (null == mates) {
            return null;
        } else {
            return mates.clone();
        }
    }

    public void setMate(final Mate[] Mates) {
        if (null == Mates) {
            this.mates = null;
        } else {
            this.mates = Mates.clone();
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Mate.class.getSimpleName() + "[", "]")
                .add("uid=" + uid)
                .add("Mate=" + Arrays.toString(mates))
                .add("mateType='" + mateType + "'")
                .toString();
    }
}
