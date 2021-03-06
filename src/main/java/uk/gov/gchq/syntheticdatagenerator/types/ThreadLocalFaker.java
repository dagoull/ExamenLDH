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

import com.github.javafaker.Faker;

import java.util.Locale;
import java.util.Random;

import static java.util.Objects.requireNonNull;

public final class ThreadLocalFaker {

    private static ThreadLocal<Faker> faker = new ThreadLocal<>();
    private static ThreadLocal<Random> currentRandom = new ThreadLocal<>();

    private ThreadLocalFaker() {
    }

    public static Faker getFaker(final Random random) {
        requireNonNull(random, "random");
        if (currentRandom.get() == null || random != currentRandom.get()) {
            //new random triggered
            currentRandom.set(random);
            faker.set(new Faker(new Locale("en-GB"), random));
        }
        return faker.get();
    }

    public void unload() {
        faker.remove();
        currentRandom.remove();
    }

}
