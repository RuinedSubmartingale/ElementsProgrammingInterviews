# Painting_iterative.cpp 848813e190b1b85a8e75107fe8513c3be38ad1a9
import sys
import random
import collections


def print_matrix(A):
    for i in A:
        print(*i, sep=' ')


# @include
def filp_color(x, y, A):
    dirs = ((0, 1), (0, -1),
            (1, 0), (-1, 0))
    color = A[x][y]

    q = collections.deque()
    A[x][y] = 1 - A[x][y]  # Flips.
    q.append((x, y))
    while q:
        curr = q.popleft()
        for d in dirs:
            nx = curr[0] + d[0]
            ny = curr[1] + d[1]
            if (nx in range(len(A)) and ny in range(len(A[nx])) and
                    A[nx][ny] == color):
                # Flips the color.
                A[nx][ny] = 1 - A[nx][ny]
                q.append((nx, ny))
# @exclude


def main():
    if len(sys.argv) == 2:
        n = int(sys.argv[1])
    else:
        n = random.randint(1, 100)

    A = []
    for i in range(n):
        A.append([random.randint(0, 1) for j in range(n)])

    i = random.randrange(n)
    j = random.randrange(n)
    print('color =', i, j, A[i][j])
    print_matrix(A)
    filp_color(i, j, A)
    print()
    print_matrix(A)


if __name__ == '__main__':
    main()
