import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import models.Dest;
import models.PersonNameList;
import models.PersonNameParts;
import models.Source;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String args[]){
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        /*
        * This part is for simple mapping and excluding a field
        * */
        mapperFactory.classMap(Source.class, Dest.class)
                .exclude("address")
                .field("name","firstName")
                .field("nickname","smallName")
                .byDefault()
                .register();

        MapperFacade mapperFacade = mapperFactory.getMapperFacade();

        Source person = new Source("aditya","aditya","delhi",16);
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
                .field("nameList[0]","firstName")
                .field("nameList[1]","lastName")
                .register();
        MapperFacade mapperFacade1 = mapperFactory1.getMapperFacade();
        List<String> nameList = Arrays.asList(new String[] {"aditya","nehra"});
        PersonNameList src = new PersonNameList(nameList);
        PersonNameParts dest = mapperFacade1.map(src,PersonNameParts.class);

        System.out.println(dest.getFirstName());

    }
}
