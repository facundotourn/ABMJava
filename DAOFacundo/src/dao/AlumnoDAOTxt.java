/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import persona.Alumno;
import persona.AlumnoInvalidoException;
import persona.FechaInvalidaException;
import persona.MiCalendar;
import persona.Persona;
import persona.PersonaInvalidaException;

/**
 *
 * @author nestor
 */
public class AlumnoDAOTxt extends DAO<Alumno, Integer>
{
    public AlumnoDAOTxt(File archivo) throws FileNotFoundException
    {
        raf = new RandomAccessFile(archivo, "rws");
    }
    
    
    @Override
    public void insertar(Alumno alu) throws DAOException
    {
        // Recibe un Alumno y agrega una linea de texto con los datos del alumno al archivo de texto.
        if(existe(alu.getDni()))
            throw new DAOException("El alumno con DNI " + alu.getDni() + " ya existe.");
        
        String linea = alu.toString() + System.lineSeparator();
        
        try
        {
            raf.seek(raf.length());
            raf.writeBytes(linea);
        }
        catch(IOException ex)
        {
            Logger.getLogger(AlumnoDAOTxt.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("No se pudo insertar: " + ex.getMessage());
        }
    }
    
    
    @Override
    public void actualizar(Alumno alumno) throws DAOException {
        try {
            raf.seek(0);

            String[] campos;
            String linea;
            
            long lineaAnterior = raf.getFilePointer();

            while((linea = raf.readLine()) != null) {
                campos = linea.split(Persona.DELIM);

                if(Integer.valueOf(campos[0]).intValue() == alumno.getDni()) {

                    linea = alumno.toString() + System.lineSeparator();
                    
                    raf.seek(lineaAnterior);
                    raf.writeBytes(linea);
                    return;
                }
                lineaAnterior = raf.getFilePointer();
            }
        } catch (IOException ex) {
            Logger.getLogger(AlumnoDAOTxt.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminar(Alumno obj) {
        try {
            obj.setEstado('B');
        } catch (AlumnoInvalidoException ex) {
            Logger.getLogger(AlumnoDAOTxt.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            actualizar(obj);
        } catch (DAOException ex) {
            Logger.getLogger(AlumnoDAOTxt.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public Alumno buscar(Integer id) throws DAOException {
        
        try {
            raf.seek(0);

            String[] campos;
            String linea;

            while((linea = raf.readLine()) != null) {
                campos = linea.split(Persona.DELIM);

                if(Integer.valueOf(campos[0]).intValue() == id.intValue()) {
                    Alumno alu = new Alumno();
                    
                    alu.setDni(Integer.valueOf(campos[0]));
                    alu.setApyn(campos[1]);
                    alu.setFechaNac(new MiCalendar(Integer.valueOf(campos[2].substring(0, 2)), Integer.valueOf(campos[2].substring(3, 5)), Integer.valueOf(campos[2].substring(6, 10))));
                    alu.setSexo(campos[3].charAt(0));
                    alu.setFechaIngr(new MiCalendar(Integer.valueOf(campos[4].substring(0, 2)), Integer.valueOf(campos[4].substring(3, 5)), Integer.valueOf(campos[4].substring(6, 10))));
                    alu.setCantMatAprob(Integer.valueOf(campos[5]));
                    alu.setEstado(campos[6].charAt(0));
                    alu.setPromedio(Double.parseDouble(campos[7]));
                    
                    return alu;
                }
            }
        } catch (IOException | PersonaInvalidaException | FechaInvalidaException ex) {
            Logger.getLogger(AlumnoDAOTxt.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        
        return null;
    }

    @Override
    public boolean existe(Integer id) throws DAOException
    {
        try
        {
            raf.seek(0);
            
            String[] campos;
            String linea;
            while((linea = raf.readLine()) != null)
            {
                campos = linea.split(Persona.DELIM);
                
                if(Integer.valueOf(campos[0]).intValue() == id.intValue())
                    return true;
            }
        }
        catch (IOException ex) {
            Logger.getLogger(AlumnoDAOTxt.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException(ex.getMessage());
        }
        
        return false;
    }

    @Override
    public List<Alumno> getTodos()
    {
        List<Alumno> alus = new ArrayList<>();
        try
        {
            raf.seek(0);
            String[] campos;
            String linea;
            
            while((linea = raf.readLine()) != null) {
                campos = linea.split(Persona.DELIM);
                
                Alumno alu = new Alumno();
                
                alu.setDni(Integer.valueOf(campos[0]));
                alu.setApyn(campos[1]);
                alu.setFechaNac(new MiCalendar(campos[2]));
                alu.setSexo(campos[3].charAt(0));
                alu.setFechaIngr(new MiCalendar(campos[4]));
                alu.setCantMatAprob(Integer.valueOf(campos[5]));
                alu.setEstado(campos[6].charAt(0));
                alu.setPromedio(Double.parseDouble(campos[7]));

                alus.add(alu);
            }
        }
        catch (FechaInvalidaException | PersonaInvalidaException | IOException ex) {
            Logger.getLogger(AlumnoDAOTxt.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return alus;
    }    
    
    private final RandomAccessFile raf;
}
