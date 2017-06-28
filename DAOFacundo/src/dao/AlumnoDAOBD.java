/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import persona.Alumno;
import persona.MiCalendar;
import persona.PersonaInvalidaException;

/**
 *
 * @author Facundo
 */
public class AlumnoDAOBD extends DAO<Alumno, Integer> {
    
    private Connection conexion;
    private PreparedStatement pStmtInsertar;
    private PreparedStatement pStmtBuscar;
    private PreparedStatement pStmtActualizar;
    private PreparedStatement pStmtGetTodos;
    private PreparedStatement pStmtEliminar;
    
    public AlumnoDAOBD() throws SQLException {
        conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/alumno", "root", "root");
        
        String sentencia =  "INSERT INTO alumnos (dni, apyn, sexo, fechaNac, promedio, cantMatAprob, fechaIngr)\n" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?);\n";
        
        pStmtInsertar = conexion.prepareStatement(sentencia);
        
        sentencia = "SELECT *\n" +
                    "FROM alumnos\n" +
                    "WHERE dni = ?;";
        
        pStmtBuscar = conexion.prepareStatement(sentencia);
        
        sentencia = "UPDATE alumnos\n" +
                    "SET apyn = ?, sexo = ?, fechaNac = ?, promedio = ?, cantMatAprob = ?, fechaIngr = ?, estado = ?\n" +
                    "WHERE dni = ?;\n";
        
        pStmtActualizar = conexion.prepareStatement(sentencia);
        
        sentencia = "SELECT * FROM alumnos;\n";
        
        pStmtGetTodos = conexion.prepareStatement(sentencia);
        
        sentencia = "DELETE FROM alumnos\n" +
                    "WHERE dni = ?;\n";
        
        pStmtEliminar = conexion.prepareStatement(sentencia);
    }
    
    @Override
    public void insertar(Alumno alu) throws DAOException {
        try {
            pStmtInsertar.setInt(1, alu.getDni());
            pStmtInsertar.setString(2, alu.getApyn());
            pStmtInsertar.setString(3, String.valueOf(alu.getSexo()));
            pStmtInsertar.setDate(4, alu.getFechaNac().toDate());
            pStmtInsertar.setDouble(5, alu.getPromedio());
            pStmtInsertar.setInt(6, alu.getCantMatAprob());
            pStmtInsertar.setDate(7, alu.getFechaIngr().toDate());
            
            pStmtInsertar.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AlumnoDAOBD.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error en insertar: " + ex.getMessage());
        }
    }

    @Override
    public void actualizar(Alumno alu) throws DAOException {
        try {
            pStmtActualizar.setString(1, alu.getApyn());
            pStmtActualizar.setString(2, String.valueOf(alu.getSexo()));
            pStmtActualizar.setDate(3, alu.getFechaNac().toDate());
            pStmtActualizar.setDouble(4, alu.getPromedio());
            pStmtActualizar.setInt(5, alu.getCantMatAprob());
            pStmtActualizar.setDate(6, alu.getFechaIngr().toDate());
            pStmtActualizar.setString(7, String.valueOf(alu.getEstado()));
            
            pStmtActualizar.setInt(8, alu.getDni());
            
            pStmtActualizar.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AlumnoDAOBD.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error en actualizar: " + ex.getMessage());
        }
    }

    @Override
    public void eliminar(Alumno alu) throws DAOException {
        try {
            pStmtEliminar.setInt(1, alu.getDni());
            
            pStmtEliminar.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AlumnoDAOBD.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error en eliminar: " + ex.getMessage());
        }
    }

    @Override
    public Alumno buscar(Integer id) throws DAOException {
        Alumno alu = null;
        
        try {
            pStmtBuscar.setInt(1, id);
            
            ResultSet rs = pStmtBuscar.executeQuery();
            
            if(!rs.next())
                return null;
            
            alu = rsAAlumno(rs);
        } catch (SQLException | PersonaInvalidaException ex) {
            Logger.getLogger(AlumnoDAOBD.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error en buscar: " + ex.getMessage());
        }
        
    
        
        return alu;
    }

    @Override
    public boolean existe(Integer id) throws DAOException {
        try {
            pStmtBuscar.setInt(1, id);
            
            ResultSet rs = pStmtBuscar.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(AlumnoDAOBD.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error en existe: " + ex.getMessage());
        }
    }

    @Override
    public List<Alumno> getTodos() throws DAOException {
        List<Alumno> alus = new ArrayList<>();
        try {
            ResultSet rs = pStmtGetTodos.executeQuery();
                       
            while(rs.next()) {
                try {
                    Alumno alu = rsAAlumno(rs);
                    alus.add(alu);
                } catch (PersonaInvalidaException ex) {
                    Logger.getLogger(AlumnoDAOBD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AlumnoDAOBD.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error obteniendo todos: " + ex.getMessage());
        }
        
        return alus;
    }
    
    private Alumno rsAAlumno(ResultSet rs) throws SQLException, PersonaInvalidaException {
        Alumno alu = new Alumno();
        
        alu.setDni(rs.getInt("dni"));
        alu.setApyn(rs.getString("apyn"));
        alu.setSexo(rs.getString("sexo").charAt(0));
        alu.setFechaNac(new MiCalendar(rs.getDate("fechaNac")));
        alu.setPromedio(rs.getDouble("promedio"));
        alu.setCantMatAprob(rs.getInt("cantMatAprob"));
        alu.setFechaIngr(new MiCalendar(rs.getDate("fechaIngr")));
        alu.setEstado(rs.getString("estado").charAt(0));
        
        return alu;
    }
    
}
