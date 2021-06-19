# Geographic Coordinate System (GIS)
---


## Introduction/Background
---
**Project description:** A geographic information system that organizes information pertaining to geographic features and provide various kinds of access
to the information. It is a system that will build and maintain several in-memory index data structures to support these operations:

- Importing new GIS records into the database file
- Retrieving data for all GIS records matching given geographic coordinates
- Retrieving data for all GIS records matching a given feature name and state
- Retrieving data for all GIS records that fall within a given (rectangular) geographic region
- Displaying the in-memory indices in a human-readable manner


This was a project for my Data Structures and Algorithms class in Spring 2021. Out of the 5 projects for that class, this was project #4, but it incorperates projects #1-3, meaning this is the result and combination of 4 projects for this class. It incorperates various data structures, concepts, and skills that include:

- Hash Table
- PR Quad Tree
- Buffer Pool
- File and String Parsing and Management
- Encapsulation, Abstraction, and Class Management

## Design Overview

---

### 1. Class Management

**MAIN**
- GIS - the main class, reads the script and processes the commands

**J4 PACKAGE**
- GISCommands - a command processor class, takes the commands given from the script and performs their actions
- GISDataBase - the class that holds the data structures and database file
- GISBufferPool - the buffer pool, holds GISFileEntry classes as entries
- GISEntry - entries that represent a record from the gis

**Name Index**
- HashTable - the hash table for name indexing, holds NameEntry classes as entries
  - the format for the key is "feature_name:country_name"
- NameEntry - entries that have the key and a list of offsets in the database file
- Hashable - interface that allows entries be hashable

**Cord Index**
- prQuadTree - the quad tree for the coord indexing, holds CordEntry classes as entries
- CordEntry - entry that holds a point and an list of offsets in the database file
- Point - a class that represents a point in the world
- Compare2D - interface that allows a comparison between cords
- Direction - enmu type that represent directions

### 2. Overview of the System Running

This system (will refer as GIS) takes in various arguments on the terminal when ran.

The GIS reads the script, passes all commands read and necessary parameters into the **GISCommands** class. The **GISCommands** class will hold a **GISDataBase** class, which maintains the table, tree, and pool for the system.

When the **world** command is ran, it will initalize the GISCommands class, creating the class that will initalize various data structures.

When the **import** command is ran, it reads the GIS record file, filling the data structures and the data base file with all records that lie within the given boundaries

When the **debug** command is ran, it writes the data structure to a human readable format to the output log

When any **search** commands are ran, it will retrieve all records that match from the data structures. For all these records:

1. It checks if it is already within the buffer pool
2. If it is already in the buffer pool, the record is already accessible
3. If it isn't in the buffer pool, it will grab the record from the database file
4. The ordering in the buffer pool is updated
5. The record is passed and the requested information is written to the output file



## Detailed Look
---

### 1. Arguments

When the GIS is ran on the terminal, it accepts 3 arguments:

- Name of database file
- Name of script file
- Name of output log file

The database file is the text file that holds all GIS records that fall within the boundaries given in the world command.
The script file is a text file of commands that the GIS will read and run.
The output log file is the text file with the results from the commands read from the script file.

Only the script file needs to exist before running this. The database file and the output log file will be created with the user given name if they do not exist at the time of running.

### 2. GIS Records

**Format of a GIS Record**

<img src="images/Java/GIS/GIS_Record_Format.png"/>

[Example of a GIS Record Data File](/text_files/GIS/VA_MontereySmall.txt)

### 3. Script Commands and Format

Each line in the script file ether starts with a ";" or not. It denotes that the line is a comment and the GIS will ignore this line.

A non-commented line denotes a command to run. There are 5 different types of commands:

- world
- import
- debug
- search
- quit

#### **World (westLong) (eastLong) (southLat) (northLat)**

This should be the first command in any script file. This sets up the boundaries for the GIS system. When importing any records, it only accepts records within this given boundary. The four parameters will be longitude and latitudes expressed in DMS format.

#### **Import (GIS record file name)**

This fills the GIS with all records that fall within the boundaries. When the GIS is being "filled", that means that record entries are filling the data structures and the database file is being written to.

#### **Debug [ quad | hash | pool ]**

This command prints to the output file a human-readable version of the selected data structure

#### **Search**

This command returns the informataion for records that are searched. There are 3 kinds of search commands:

```
what_is_at (geographic coordinate)
For every GIS record in the database file that matches the given <geographic coordinate>, log the offset at
which the record was found, and the feature name, county name, and state abbreviation

what_is (feature name) (state abbreviation)
For every GIS record in the database file that matches the given <feature name> and <state
abbreviation>, log the offset at which the record was found, and the county name, the primary latitude, and the
primary longitude

what_is_in (geographic coordinate) (half-height) (half-width)
For every GIS record in the database file whose coordinates fall within the closed rectangle with the specified height
and width, centered at the <geographic coordinate>, log the offset at which the record was found, and the
feature name, the state name, and the primary latitude and primary longitude.

If a (geographic coordinate) is specified for a command, it will be expressed as a pair of latitude/longitude values,
expressed in the same DMS format that is used in the GIS record files
  
```

#### **Quit**

The last command in the script. It signals to the GIS that the script file is over

[Example of a Script File](/text_files/GIS/DemoScript05.txt)

[Example of a Output Log File](/text_files/GIS/DemoLog05.txt)



## Download/Code

---

This was a project for Data Structures and Algorithms class in Spring 2021. Due to honor code I am unable to show the code publically. If you wish to see it, please reach out to my email brnguyen2017@gmail.com


## Personal Thoughts

---

### Design Consideration

As this project was a combination of multiple projects, it was more massive than any previous projects the entire semester. This meant that analyzing and planning the system was much more important. While I analyzed and mapped out the class and structure, I kept two main things in focused on: "independency" and readability. 

**Independency:** As I mapped out what classes would be written in the outline, I did my best to have all classes self contained and all classes were easily defined with a single sentence or two about its purpose. This supports the idea of abstraction and code re-usability. 

**Readability:** As all my code, I make sure to document and comment well enough where if I were to pass this code to another person to maintain/update, they would be able to easily read and understand the overall system.

