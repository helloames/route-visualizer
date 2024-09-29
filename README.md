# Project 6: Route

To run the project, see GraphDemo.java and /docs/details.md. If you have questions, email me at amy.zhang@duke.edu. Thanks!

This program finds the shortest path between two cities - using exclusively highways - in the US. The program uses a graph data structure to represent the map of the US, implemented using an adjacency list. We then use Dijkstra's algorithm using a priority queue to find the shortest path between the specified two cities. The taken path is then displayed on a map of the US using Matplotlib. This can be found in the Visualize.py file.

To run the program, run the ShortestPath.py file. The file will open a tkinter interface to prompt the starting and ending city, which sould be written in the format: "CityName - StateAbbreviation" i.e: "Seattle WA" or "Miami FL". A list of all cities is available in the csv file - the program will not run until both the start and end cities are valid. The program will then display the path, as well as the total distance of the path in miles in the same interface. The path is displayed in blue, the starting city in green, and the ending city in red.
