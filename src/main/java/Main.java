import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import models.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String args[]) {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        /*
         * This part is for simple mapping and excluding a field
         * */
        mapperFactory.classMap(Source.class, Dest.class)
                .exclude("address")
                .field("name", "firstName")
                .field("nickname", "smallName")
                .byDefault()
                .register();

        MapperFacade mapperFacade = mapperFactory.getMapperFacade();

        Source person = new Source("aditya", "aditya", "delhi", 16);
        Dest person1 = mapperFacade.map(person, Dest.class);

        System.out.println(person1);


        /*
         * Collections Mapping
         *
         * Sometimes the destination object may have unique attributes while the source object just maintains
         * every property in collection.
         * */
        MapperFactory mapperFactory1 = new DefaultMapperFactory.Builder().build();
        mapperFactory1.classMap(PersonNameList.class, PersonNameParts.class)
                .field("nameList[0]", "firstName")
                .field("nameList[1]", "lastName")
                .register();
        MapperFacade mapperFacade1 = mapperFactory1.getMapperFacade();
        List<String> nameList = Arrays.asList(new String[]{"aditya", "nehra"});
        PersonNameList src = new PersonNameList(nameList);
        PersonNameParts dest = mapperFacade1.map(src, PersonNameParts.class);

        System.out.println(dest.getFirstName());

        /*
         * Maps
         *
         * Assuming our source object has a map of values. We know there is a key in that map, first, whose value
         * represents a person’s firstName in our destination object.
         * */
        MapperFactory mapperFactory2 = new DefaultMapperFactory.Builder().build();
        mapperFactory2.classMap(PersonNameMap.class, PersonNameParts.class)
                .field("nameMap[\"first\"]", "firstName")
                .field("nameMap[\"last\"]", "lastName")
                .register();

        MapperFacade mapperFacade2 = mapperFactory2.getMapperFacade();
        Map<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("first", "Leonardo");
        nameMap.put("last", "Dicaprio");
        PersonNameMap personNameMap = new PersonNameMap(nameMap);
        PersonNameParts personNameParts = mapperFacade2.map(personNameMap, PersonNameParts.class);
        System.out.println(personNameParts.getFirstName());

        /*
        * Map Nested field
        *
        * Following on from the preceding collections examples, assume that inside our source data object, there is
        * another Data Transfer Object (DTO) that holds the values we want to map.
        * */
        MapperFactory mapperFactory3 = new DefaultMapperFactory.Builder().build();
        mapperFactory3.classMap(PersonContainer.class, PersonNameParts.class)
                .field("name.firstName","firstName")
                .field("name.lastName","lastName")
                .register();
        MapperFacade mapperFacade3 = mapperFactory3.getMapperFacade();
        PersonContainer personContainer = new PersonContainer(new Name("Nick","Canon"));
        PersonNameParts personNameParts1 = mapperFacade3.map(personContainer,PersonNameParts.class);
        System.out.println(personNameParts1.getLastName());

        /*
        * Mapping NULL values
        *
        * In some cases, you may wish to control whether nulls are mapped or ignored when they are encountered.
        * By default, Orika will map null values when encountered:
        * */
        MapperFactory mapperFactory4 = new DefaultMapperFactory.Builder().build();
        mapperFactory4.classMap(NameSource.class,NameDest.class)
                .field("firstName","name")
                .field("lastName","surName")
                .byDefault()
                .register();
        MapperFacade mapperFacade4 = mapperFactory4.getMapperFacade();
        NameSource nameSource = new NameSource("aditya",null);
        NameDest nameDest = mapperFacade4.map(nameSource,NameDest.class);
        System.out.println(nameDest.getName() + " :: " + nameDest.getSurName());
    }
}
