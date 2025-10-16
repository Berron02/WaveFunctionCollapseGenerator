# WaveFunctionCollapseGenerator
The goal of the algorithm is to produce a coherent distribution of patterns over a grid of size
𝑛 × 𝑚, where the resulting values can be used as a basis for rendering 2D or 3D scenes. The generation process is semi-random: while preserving a degree of randomness, the choices are guided by compatibility constraints between adjacent elements.
<br>
![img.png](image/img.png)
<br>

Let's say we have these pieces<br>
![immagine0.jpg](image/immagine0.jpg)
![immagine1.jpg](image/immagine1.jpg)
![immagine2.jpg](image/immagine2.jpg)
<br>
![immagine3.jpg](image/immagine3.jpg)
![immagine4.jpg](image/immagine4.jpg)
![immagine5.jpg](image/immagine5.jpg)
<br>
![immagine6.jpg](image/immagine6.jpg)
![immagine7.jpg](image/immagine7.jpg)
<br>
Each of them is numbered from 1 to 8 and we imagine that each image is made up of a 3x3 matrix, where the white lines are indicated by 1 and the black areas by 0.

![img.png](img.png)

The program takes two arguments as input from the terminal:
- the number of rows: ROW
- the number of columns: COL
<br>

After receiving these two arguments, it begins by reading a dictionary, which contains a collection of definitions for the puzzle pieces to be used in the generation process. Each piece is represented by a 3x3 matrix composed of binary values (0 or 1), which describe its structure and properties.
Before proceeding, it's important to note that a custom class named Cell was developed specifically for this project.
This class is used to construct the grid.
After reading the dictionary, the next step involves initializing each cell in the grid. During this initialization, the following parameters are passed to the Cell constructor:
- the position of the cell within the grid
- the initial value (set to -1)
- a boolean flag indicating whether the cell has been collapsed
- the reference dictionary containing all valid patterns
<br>

After the initialization phase, a random cell is selected from the grid.
A random value is then assigned to this cell, chosen from the set of available options defined in the dictionary (in this case, values range from 0 to 7).

At this point, the cell is considered collapsed to a specific value, and its corresponding boolean flag is set to true.
This state change is then propagated to all adjacent cells, updating their possible options accordingly based on compatibility constraints.



