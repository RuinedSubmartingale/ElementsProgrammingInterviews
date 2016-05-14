//
// Created by psingh on 4/11/16.
//

#ifndef EPI_SOLUTIONS_CPP_5_1_PARITY_H
#define EPI_SOLUTIONS_CPP_5_1_PARITY_H

#include <array>

// using std::array;

namespace Parity1 {
    short Parity(unsigned long x) {
        short result = 0;
        while(x) {
            result ^= (x & 1);
            x >>= 1;
        }
        return result;
    }

    std::array<short, 1 << 16> BuildTable() {
        std::array<short, 1 << 16> result;
        for (int i = 0; i < (1 << 16); ++i) {
            result[i] = Parity1::Parity(i);
        }
        return result;
    };
}

namespace Parity2 {
    short Parity(unsigned long x) {
        short result = 0;
        while(x) {
            result ^= 1;
            x &= (x-1);
        }
        return result;
    }
}

namespace Parity3 {
    static std::array<short, 1 << 16> precomputed_parity = Parity1::BuildTable();
    short Parity(unsigned long x) {
        const int kWordSize = 16;
        const int kBitMask = 0xFFFF;
        return precomputed_parity[x >> (3 * kWordSize) & kBitMask] ^
                precomputed_parity[x >> (2 * kWordSize) & kBitMask] ^
                precomputed_parity[x >> (1 * kWordSize) & kBitMask] ^
                precomputed_parity[x & kBitMask];
    }
}

namespace Parity4 {
    short Parity(unsigned long x) {
        x ^= (x >> 32);
        x ^= (x >> 16);
        x ^= (x >> 8);
        x ^= (x >> 4);
        x ^= (x >> 2);
        x ^= (x >> 1);
        return (short) (x & 0x1);
    }
}

#endif //EPI_SOLUTIONS_CPP_5_1_PARITY_H
