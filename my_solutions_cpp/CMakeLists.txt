cmake_minimum_required(VERSION 3.5)
project(EPI_Solutions_cpp)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES 0-1_knapsack.cpp my_solutions/5-1-Parity.cpp)
add_executable(EPI_Solutions_cpp ${SOURCE_FILES})

set(BOOK_SOLUTIONS_DIR book_solutions)
add_subdirectory(${BOOK_SOLUTIONS_DIR})
# include_directories( ${BOOK_SOLUTIONS_DIR}/src )