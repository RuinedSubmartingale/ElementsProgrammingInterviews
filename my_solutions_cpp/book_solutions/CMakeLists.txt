cmake_minimum_required(VERSION 3.5)
project(EPI_Solutions_cpp/book_solutions)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES 0-1_knapsack.cc Parity.cc)
add_executable(book_solutions ${SOURCE_FILES})
