package com.github.ysbbbbbb.kaleidoscopecookery.util;

import com.mojang.datafixers.util.Function9;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface StreamCodecUtil {
    static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9> StreamCodec<B, C> composite(
            final StreamCodec<? super B, T1> codec1,
            final Function<C, T1> getter1,
            final StreamCodec<? super B, T2> codec2,
            final Function<C, T2> getter2,
            final StreamCodec<? super B, T3> codec3,
            final Function<C, T3> getter3,
            final StreamCodec<? super B, T4> codec4,
            final Function<C, T4> getter4,
            final StreamCodec<? super B, T5> codec5,
            final Function<C, T5> getter5,
            final StreamCodec<? super B, T6> codec6,
            final Function<C, T6> getter6,
            final StreamCodec<? super B, T7> codec7,
            final Function<C, T7> getter7,
            final StreamCodec<? super B, T8> codec8,
            final Function<C, T8> getter8,
            final StreamCodec<? super B, T9> codec9,
            final Function<C, T9> getter9,
            final Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, C> factory
    ) {
        return new StreamCodec<>() {
            @Override
            public @NotNull C decode(B buf) {
                T1 v1 = codec1.decode(buf);
                T2 v2 = codec2.decode(buf);
                T3 v3 = codec3.decode(buf);
                T4 v4 = codec4.decode(buf);
                T5 v5 = codec5.decode(buf);
                T6 v6 = codec6.decode(buf);
                T7 v7 = codec7.decode(buf);
                T8 v8 = codec8.decode(buf);
                T9 v9 = codec9.decode(buf);
                return factory.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9);
            }

            @Override
            public void encode(B buf, C value) {
                codec1.encode(buf, getter1.apply(value));
                codec2.encode(buf, getter2.apply(value));
                codec3.encode(buf, getter3.apply(value));
                codec4.encode(buf, getter4.apply(value));
                codec5.encode(buf, getter5.apply(value));
                codec6.encode(buf, getter6.apply(value));
                codec7.encode(buf, getter7.apply(value));
                codec8.encode(buf, getter8.apply(value));
                codec9.encode(buf, getter9.apply(value));
            }
        };
    }
}
