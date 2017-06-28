/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persona;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author nestor
 */
public class MiCalendar extends GregorianCalendar
{
    public MiCalendar(Integer día, Integer mes, Integer año) throws FechaInvalidaException
    {
        super(año, mes-1, día);
        
        setLenient(false);
        
        try
        {
            get(DAY_OF_MONTH);
        }
        catch(IllegalArgumentException ex)
        {
            throw new FechaInvalidaException("El día es inválido.");
        }
        
        try
        {
            get(MONTH);
        }
        catch(IllegalArgumentException ex)
        {
            throw new FechaInvalidaException("El mes es inválido.");
        }
        
        try
        {
            get(YEAR);
        }
        catch(IllegalArgumentException ex)
        {
            throw new FechaInvalidaException("El año es inválido.");
        }
    }
    
    
    public MiCalendar(Calendar calendar) throws FechaInvalidaException
    {
        setDía(calendar.get(DAY_OF_MONTH));
        setMes(calendar.get(MONTH) + 1);
        setAño(calendar.get(YEAR));
    }
    
    public MiCalendar(String cadena) throws FechaInvalidaException {
        setDía(Integer.valueOf(cadena.substring(0, 2)));
        setMes(Integer.valueOf(cadena.substring(3, 5)));
        setAño(Integer.valueOf(cadena.substring(6, 10)));
    }

    public MiCalendar(Date date) {
        this.setTimeInMillis(date.getTime());
    }
    
    
    void setDía(int día) throws FechaInvalidaException
    {
        set(DAY_OF_MONTH, día);
        
        try
        {
            get(DAY_OF_MONTH);
        }
        catch(IllegalArgumentException ex)
        {
            throw new FechaInvalidaException("El día es inválido.");
        }        
    }
    
    
    public int getDía()
    {
        return get(DAY_OF_MONTH);
    }
    
    
    public void setMes(int mes) throws FechaInvalidaException
    {
        set(MONTH, mes-1);
        
        try
        {
            get(MONTH);
        }
        catch(IllegalArgumentException ex)
        {
            throw new FechaInvalidaException("El mes es inválido.");
        }
    }
    
    
    public int getMes()
    {
        return get(MONTH)+1;
    }
    
    
    public void setAño(int año) throws FechaInvalidaException
    {
        set(YEAR, año);
        
        try
        {
            get(YEAR);
        }
        catch(IllegalArgumentException ex)
        {
            throw new FechaInvalidaException("El año es inválido.");
        }        
    }
    
    
    public int getAño()
    {
        return get(YEAR);
    }
    
    
    @Override
    public String toString()
    {
        return String.format("%02d", getDía()) + "/" + String.format("%02d", getMes()) + "/" + getAño();
    }

    public Date toDate() {
        return new Date(getTimeInMillis());
    }
    
    
    
}
