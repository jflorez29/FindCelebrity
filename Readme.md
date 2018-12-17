## **Suppositions**

- Only can there one celebrity  
- Not necessary everyone know each others  
- There may not be any celebrity

## **Explanation solution**

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
    /api/find-celebrity 
    file=@file.csv

*Second endpoint*

    Method: GET
    /api/find-celebrity/{idProcess}
    
  

## **How build**

    mvn clean install

## **How run**

    mvn spring-boot:run
