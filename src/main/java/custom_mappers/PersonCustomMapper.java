package models.custom_mappers;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MappingContext;
import models.Person;
import models.Personne;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/*
* Notice that we have implemented methods mapAtoB and mapBtoA. Implementing both makes our mapping function bi-directional.
* */
public class PersonCustomMapper extends CustomMapper<Person, Personne> {

    @Override
    public void mapAtoB(Person a, Personne b, MappingContext context){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = null;
        try {
            date = format.parse(a.getDtob());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timestamp = date.getTime();
        b.setDtob(timestamp);
        b.setName(a.getName());
    }

    @Override
    public void mapBtoA(Personne b, Person a, MappingContext context) {
        Date date = new Date(b.getDtob());
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String isoDate = format.format(date);
        a.setDtob(isoDate);
        a.setName(b.getName());
    }

}
