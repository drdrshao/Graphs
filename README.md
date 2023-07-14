# A2 Graphs
George Shao (g3shao 20849675)

## Setup
* Windows 11
* IntelliJ IDEA 2022.2.3 (Community Edition)
* kotlin.jvm 1.7.10
* Java SDK 17.0.2 (temurin)

## Basic
1. Users cannot have two datasets with the same name, a popup saying: You cannot add a dataset that has the same name as an existing dataset.

2. The scroll bar will appear if there are more entry in the data entry section.

3. For piazza post @180, when selecting dataset, the visualization stays the same as the graph before selection.

4. Bonus parts added.

## Enhancement 
I added the following enhancement:
- User can visualize the dataset using Scatter Plot
1. There is a button named "Scatter" in the visualization selector toolbar, user can click it so that the scatter plot of the selected dataset will be shown on the right visualization section.

- The bar graph is now enhanced with Confidence Interval
1. The button is changed from "Bar (SEM)" to "Bar (Enhanced)" to address this change.
2. The graph will have two more dashed lines in red to indicate the confidence interval.
3. The interval will also be shown on the top-left corner in text.
4. The calculation of the confidence interval is based on an assumption that the dataset is drawn from a normal distribution.
