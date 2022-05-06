%returns true if given two users are friends or friends of friends
areTransFriends(A,B) :-   
  walk(A,B,[]) -> true.         

walk(A,B,V) :-       
  not(permission_exclude(A, B)),
  not(permission_exclude(B, A)),
  areDirectFriends(A,X),   
  not(permission_exclude(A, X)),
  not(permission_exclude(X, A)), 
  not(permission_exclude(X, B)),
  not(permission_exclude(B, X)),    
  not(member(X,V)),
  (B = X; walk(X,B,[A|V])).                  

%returns true if users are friends
areDirectFriends(User1, User2) :-
    friended(User1, User2) ; friended(User2, User1).

%returns returns true if a post contains word
containsWord(User, (ID, M), Word) :-
       post(ID, User, M),
       sub_atom(M, _, Length, _, Word),
       Length > 0 -> true.

%returns true if post contains a bad word
containsBadWords(User, (ID, M)) :-
       containsWord(User, (ID, M), 'bong') ;
       containsWord(User, (ID, M), 'wasted') ;
       containsWord(User, (ID, M), 'drinking') ;
       containsWord(User, (ID, M), 'snorted').


%returns true if likes exceed 20% of friends
likesExceed20PercentOfFriends(User, (ID)) :-
       aggregate_all(count, areDirectFriends(User, _), FriendsCount),
       aggregate_all(count, likes(_, ID), LikesCount),
       LikesCount / FriendsCount > 0.2 -> true.

%returns true if comments exceed 30% of friends
commentsExceed30PercentOfFriends(User, (ID)) :-
       aggregate_all(count, areDirectFriends(User, _), FriendsCount),
       aggregate_all(count, comment(_, ID, _), LikesCount),
       LikesCount / FriendsCount > 0.3 -> true.

%returns all posts that this interviewer can see
/*
if user has direct friends only and interviewer isn't direct friend -> false
if user are friends by trans and user does not exclude interviewer -> true
else check if interviewer can reach user post
*/
viewablePosts(User, Interviewer, (ID, M)) :-
       permission_mfo(User), not(areDirectFriends(User, Interviewer)) -> false ;
       permission_mfo(User), areDirectFriends(User, Interviewer) -> post(ID, User, M) ;
       areTransFriends(User, Interviewer), not(permission_exclude(User, Interviewer)) -> post(ID, User, M) ;
       areTransFriends(User, Interviewer) -> post(ID, User, M).

%returns true if the interviewer can see the user post
%canSeePost(User, Interviewer, (ID, M)) :-

       
test(A, B) :- setof(X, friended(A, X), B).

test2(User, Interviewer, S) :- setof((ID, M), toBeCheckedPost(User, Interviewer, (ID, M)), S).


toBeCheckedPost(User, Interviewer, (ID, M)) :-
       viewablePosts(User, Interviewer, (ID, M)), (containsBadWords(User, (ID, M)), likesExceed20PercentOfFriends(User, (ID)) ; commentsExceed30PercentOfFriends(User, (ID))).
       