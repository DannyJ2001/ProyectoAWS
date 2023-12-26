package com.example.ProyectoAWS.controller;

import com.example.ProyectoAWS.model.Empleado;
import com.example.ProyectoAWS.service.EmpleadoService;
import com.example.ProyectoAWS.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController  {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private S3Service s3Service;

    @GetMapping
    public List<Empleado> getAll() {
        return empleadoService.findByAll()
                .stream()
                .peek(empleado -> empleado.setImagenUrl(s3Service.getObjectUrl(empleado.getImagenPath())))
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<Empleado> create(@RequestBody Empleado empleado) {
        try {
            empleadoService.save(empleado);
            empleado.setImagenUrl(s3Service.getObjectUrl(empleado.getImagenPath()));
            // Puedes agregar más lógica según sea necesario, como configurar la URL de la cédula, etc.
            return new ResponseEntity<>(empleado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findId/{id}")
    public ResponseEntity<Empleado> findById(@PathVariable("id") Integer id) {
        try {
            return ResponseEntity.ok(empleadoService.findById(id));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Empleado> saveEmpleado(@RequestBody Empleado empleado) {
        try {
            return new ResponseEntity<>(empleadoService.save(empleado), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmpleado(@PathVariable("id") Integer id) {
        try {
            empleadoService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Correcto al eliminar al empleado");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEmpleado(@RequestBody Empleado empleado, @PathVariable("id") Integer id) {
        Empleado empleadoUp = empleadoService.findById(id);

        if (empleadoUp == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se ha podido encontrar a este empleado");
        } else {
            try {
                empleadoUp.setNombre(empleado.getNombre());
                empleadoUp.setClave(empleado.getClave());
                empleadoUp.setEmail(empleado.getEmail());
                empleadoUp.setEstado(empleado.getEstado());
                return new ResponseEntity<>(empleadoService.save(empleadoUp), HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}