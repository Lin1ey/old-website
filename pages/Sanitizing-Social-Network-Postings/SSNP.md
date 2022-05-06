# Sanitizing-Social-Network-Postings

## What is this?

A program that returns "questionable posts" of a candidate's social network. Given a candidate and interviewer, following the social network's visibility rules, it will return a list of the candidate's posts that are questionable

Questionable posts are one of the following:

1. Contains the words "drinking", "wasted", "snorted", or "bong" AND has  been liked by more than 20% of the poster's friends
2. Has been commented on by more than 30% of the poster's friends

The social network's visibility rules makes a user's post visible by:

1. Friends
2. Friends of Friends, defined transitively (i.e., firned of a friend of a friend...)
3. Some users have restricted the visibility of their posts, so we only check posts that are visable by an interviewer

## Details

A folder of a candidate's info has the following csv files:

#### 1. Comments
The comments and their commenter on the candidate's posts.

#### 2. Friends
To show who are direct friends with others

### 3. Likes
The people who like the candidate's posts

### 4. Posts
The posts of the candidate

### 5. Permission settings for "my friends only"
Setting to set that only direct friends can see direct posts

### 6. Permission Exclude
Permission settings to exclude a user to seeing the poster's posts even though they are friends (they are still able to see the viewer's posts by being friends with the poster's friends)

## How to use

### Make Run
Using the command

```make candidate=(candidate-name) interviewer=(interviewer-name) CSVDir=(path-to-folder-of-candidate's-info) run```

to create a **candidate_interviewer.csv** file with all the questionable posts

### Make Clean
  
Using the make clean will clear all generated files from the Makefile

## Screenshots

### Showcase of running the Makefile

![Showcasing-make-run](/pages/Sanitizing-Social-Network-Postings/images/showcase_of_running_the_code.jpg "make run")

Output of Makefile underlined in red

### Showcase of cleaning the files created by the Makefile

![Showcasing-make-clean](/pages/Sanitizing-Social-Network-Postings/images/showcase_of_cleaning_up.PNG "make clean")

### Candidate Ellen and Interviewer Rich Results

![Ellen-Rich](/pages/Sanitizing-Social-Network-Postings/images/ellen_rich_result.PNG "ellen and rich results")

### Candidate Li and Interviewer Annie Results

![Li-Annie](/pages/Sanitizing-Social-Network-Postings/images/li_annie_result.PNG "li and annie results")

### Candidate Li and Interviewer Jose Results

![Li-Jose](/pages/Sanitizing-Social-Network-Postings/images/li_jose_result.PNG "li and hose results")
