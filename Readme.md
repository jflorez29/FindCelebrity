## **Suppositions**

- Only can there one celebrity  
- Not necessary everyone know each others  
- There may not be any celebrity

## **Explanation solution**

Used Stack method to solve problem.

Add all of people to stack, while stack contains two or more persons take two person of them from top and asked to first person if knows second person, if knows means second person can be a celebrity else firs person can be.

After that, when stack has only one person who is a possible celebrity, run validation asking all people if they know the possible celebrity, if they know then return celebrity otherwise return message "Not found celebrity"

## **Explanation implementation**
Created a Rest service with two endpoints 

***First endpoint***

Sent file with data, if exists celebrity return it, else return message "Not celebrity found"

A csv file must be sent as Multipart request with people's data in the following format 

When person has unique  relationship

    {idPerson}:{idPersonRelation};

**E.G**. `1:1;`

When person has many relationships

    {idPerson}:{idPersonRelation1},{idRelationN};

**E.G**. `4:2,5,6,7;`

    Method: POST  
    /api/find-celebrity/process
    file=@file.csv

*Second endpoint*

Used to query result from database using process id

    Method: GET
    /api/find-celebrity/process/{idProcess}
    
  

## **How build**

    mvn clean install

## **How run**

    mvn spring-boot:run
