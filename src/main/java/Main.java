import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import models.*;
import models.custom_mappers.PersonCustomMapper;

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
                .field("name.firstName", "firstName")
                .field("name.lastName", "lastName")
                .register();
        MapperFacade mapperFacade3 = mapperFactory3.getMapperFacade();
        PersonContainer personContainer = new PersonContainer(new Name("Nick", "Canon"));
        PersonNameParts personNameParts1 = mapperFacade3.map(personContainer, PersonNameParts.class);
        System.out.println(personNameParts1.getLastName());

        /*
         * Mapping NULL values
         *
         * In some cases, you may wish to control whether nulls are mapped or ignored when they are encountered.
         * By default, Orika will map null values when encountered:
         * */
        MapperFactory mapperFactory4 = new DefaultMapperFactory.Builder().build();
        mapperFactory4.classMap(NameSource.class, NameDest.class)
                .field("firstName", "name")
                .field("lastName", "surName")
                .byDefault()
                .register();
        MapperFacade mapperFacade4 = mapperFactory4.getMapperFacade();
        NameSource nameSource = new NameSource("aditya", null);
        NameDest nameDest = mapperFacade4.map(nameSource, NameDest.class);
        System.out.println(nameDest.getName() + " :: " + nameDest.getSurName());


        /*
         * Global Configuration
         *
         * We can configure our mapper to map nulls or ignore them at the global level before creating the global
         * MapperFactory.
         * */
        MapperFactory mapperFactory5 = new DefaultMapperFactory.Builder().mapNulls(false).build();
        mapperFactory5.classMap(NameSource.class, NameDest.class)
                .field("firstName", "name")
                .field("lastName", "surName")
                .byDefault()
                .register();
        MapperFacade mapperFacade5 = mapperFactory5.getMapperFacade();
        NameSource nameSource1 = new NameSource("mike", null);
        NameDest nameDest1 = new NameDest("hilliary", "clinton");
        mapperFacade5.map(nameSource1, nameDest1);
        //What happens is that, by default, nulls are mapped. This means that even if a field value in the source object
        //is null and the corresponding field’s value in the destination object has a meaningful value, it will be overwritten.
        System.out.println(nameDest1.getName() + " :: " + nameDest1.getSurName());


        /*
         * Local Configuration
         *
         * Mapping of null values can be controlled on a ClassMapBuilder by using the mapNulls(true|false) or
         * mapNullsInReverse(true|false) for controlling mapping of nulls in the reverse direction.
         *
         * By setting this value on a ClassMapBuilder instance, all field mappings created on the same ClassMapBuilder,
         * after the value is set, will take on that same value.
         * */
        MapperFactory mapperFactory6 = new DefaultMapperFactory.Builder().build();
        mapperFactory6.classMap(NameSource.class, NameDest.class)
                .field("firstName", "name")
                .mapNulls(false)
                .field("lastName", "surName")
                .byDefault()
                .register();
        MapperFacade mapperFacade6 = mapperFactory6.getMapperFacade();
        NameSource nameSource2 = new NameSource("aditya", null);
        NameDest nameDest2 = new NameDest("hilliary", "clinton");
        mapperFacade6.map(nameSource2, nameDest2);
        // Notice how we call mapNulls just before registering lastName field, this will cause all fields following the
        // mapNulls call to be ignored when they have null value.
        System.out.println(nameDest2.getName() + " :: " + nameDest2.getSurName());

        NameSource nameSource3 = new NameSource(null, "nehra");
        NameDest nameDest3 = new NameDest("hilliary", "clinton");
        mapperFacade6.map(nameSource3, nameDest3);
        // Notice how we call mapNulls after registering firstName field, this will cause null of firstName field to
        //copy to name field in nameDest3.
        System.out.println(nameDest3.getName() + " :: " + nameDest3.getSurName());

        /*
         * Bi-Directional mapping also accepts mapped null values
         * */
        MapperFactory mapperFactory7 = new DefaultMapperFactory.Builder().build();
        mapperFactory7.classMap(NameSource.class, NameDest.class)
                .field("firstName", "name")
                .field("lastName", "surName")
                .register();
        MapperFacade mapperFacade7 = mapperFactory7.getMapperFacade();
        NameSource nameSource4 = new NameSource("aditya", "nehra");
        NameDest nameDest4 = new NameDest(null, "cilton");
        mapperFacade7.map(nameDest4, nameSource4);
        System.out.println(nameSource4.getFirstName() + " :: " + nameSource4.getLastName());


        /*
         * We can prevent this by calling mapNullInReverse and passing in false
         * */
        MapperFactory mapperFactory8 = new DefaultMapperFactory.Builder().build();
        mapperFactory8.classMap(NameSource.class, NameDest.class)
                .field("firstName", "name")
                .mapNullsInReverse(false)
                .field("lastName", "surName")
                .register();
        MapperFacade mapperFacade8 = mapperFactory8.getMapperFacade();
        NameSource nameSource5 = new NameSource("aditya", "nehra");
        NameDest nameDest5 = new NameDest(null, "cilton");
        mapperFacade8.map(nameDest5, nameSource5);
        //Here null will be mapped because mapNullsInReverse is done false after mapping 'firstName' to 'name'.
        System.out.println(nameSource5.getFirstName() + " :: " + nameSource5.getLastName());

        NameSource nameSource6 = new NameSource("aditya", "nehra");
        NameDest nameDest6 = new NameDest("hilliary", null);
        mapperFacade8.map(nameDest6, nameSource6);
        //Here null will not be mapped because mapNullsInReverse is done false before mapping 'lastname' to 'surName'
        System.out.println(nameSource6.getFirstName() + " :: " + nameSource6.getLastName());

        /*
        * Field Level configuration
        *
        * */
        MapperFactory mapperFactory9 = new DefaultMapperFactory.Builder().build();
        mapperFactory9.classMap(NameSource.class,NameDest.class)
                .fieldMap("firstName","name").mapNulls(false).add()
                .field("lastName","surName")
                .byDefault()
                .register();
        MapperFacade mapperFacade9 = mapperFactory9.getMapperFacade();
        NameSource nameSource7 = new NameSource(null,"nehra");
        NameDest nameDest7 = new NameDest("hilliary","clinton");
        mapperFacade9.map(nameSource7, nameDest7);
        //In this case, the configuration will only affect the name field as we have called it at field level:
        System.out.println(nameDest7.getName() + " :: " + nameDest7.getSurName());

        NameSource nameSource8 = new NameSource("aditya",null);
        NameDest nameDest8 = new NameDest("hilliary","clinton");
        mapperFacade9.map(nameSource8,nameDest8);
        // In this case the null will be copied because mapsNull is applied only on 'firstName'
        System.out.println(nameDest8.getName() + " :: " + nameDest8.getSurName());


        /*
        * Orika Custom Mapping
        *
        * how to convert to object of different type like
        * 2007-06-26T21:22:39Z
        *       to
        * 1182882159000
        *
        * Clearly, non of the customizations we have covered so far suffices to convert between the two formats during
        * the mapping process, not even Orika’s built in converter can handle the job. This is where we have to write a
        * CustomMapper to do the required conversion during mapping.
        * */
        MapperFactory mapperFactory10 = new DefaultMapperFactory.Builder().build();
        CustomMapper custom = new PersonCustomMapper();
        mapperFactory10.classMap(Person.class, Personne.class)
                .customize(custom)
                .register();
        MapperFacade mapperFacade10 = mapperFactory10.getMapperFacade();
        String dateTime = "2007-06-26T21:22:39Z";
        long timestamp = new Long("1182882159000");
        Person person2 = new Person("leonardo ", dateTime.toString());
        Personne personne = mapperFacade10.map(person2,Personne.class);
        System.out.println(personne.getName() + " :: " + personne.getDtob());
        
    }
}
