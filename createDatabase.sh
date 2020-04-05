#!/bin/bash

mysql -uroot -pbd2 < db.sql
echo "Se creo el usuario:"
echo ""
echo "Nombre de usuario:   grupo21"
echo "Password:   1234"
echo "Base de datos:	bd2_grupo21"