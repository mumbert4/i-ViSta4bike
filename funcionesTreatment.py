#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Tue Oct 31 18:36:58 2023

@author: ramitjans
"""

import json
import joblib
import pandas as pd
import numpy as np
from datetime import datetime, timedelta
import requests

import re
import networkx as nx
import matplotlib.pyplot as plt

from sklearn.preprocessing import MinMaxScaler


# =============================================================================
def creacionEstaciones(ruta):
    with open(ruta + 'estacionesCercanas.json', 'r', encoding='utf-8') as json_file:
        estacionesCercanas = json.load(json_file)
    return(estacionesCercanas)

# =============================================================================
def subsetBarrio(data, barrio, estaciones):
    estacionesSeleccionadas = estaciones[barrio]
    # Condiciones para Start Station Id y End Station Id
    cond1 = data['Start Station Id'].isin(estacionesSeleccionadas)
    cond2 = data['End Station Id'].isin(estacionesSeleccionadas)
    # Selecciona los registros que cumplan ambas condiciones
    seleccion = data[cond1 & cond2]
    return(seleccion)

# =============================================================================
def subsetFranja(data, franja):

    franjas = {
        "Morning": [0, 6],
        "Afternoon": [6, 12],
        "Evening": [12, 18],
        "LateEvening": [18, 21],
        "Night": [21, 24]}

    franjaSeleccionadas = franjas[franja]

    # Transformamos las
    data['Start Time'] = pd.to_datetime(data['Start Time'])
    data['Start Hour'] = [m.hour for m in data['Start Time']]
    data['End Time'] = pd.to_datetime(data['End Time'])
    data['End Hour'] = [m.hour for m in data['End Time']]

    # Creamos los condicionantes para la hora de inicio y de final
    cond1 = (data['Start Hour'] >= franjaSeleccionadas[0])
    cond2 = (data['End Hour'] <= franjaSeleccionadas[1])

    # Eliminamos las columnas que no es necesario
    data = data.drop(["Start Hour", "End Hour"], axis=1)

    # Aplicamos las condiciones
    seleccion = data[cond1 & cond2]
    return(seleccion)

# =============================================================================
def convertirMatriz(data, arrays = False):
    if arrays == False:
        conteo = data.groupby(
            ['Start Station Id', 'End Station Id']).size().reset_index(name='Conteo')
        # Creamos la matriz
        matriz = conteo.pivot(index='End Station Id', columns='Start Station Id',
                              values='Conteo')
        # Rellenamos los NAs con 0
        matriz = matriz.fillna(0)
    
    if arrays == True:
        matriz = np.array(data)
    
    return(matriz)

# =============================================================================
def retornaModel(path, barri):
    match = re.search(r'\d+', barri).group()
    patron = "modelo_knn_barri_" + str(match) + ".joblib"
    modeloCargado = joblib.load(path + patron)
    return(modeloCargado)

# =============================================================================
def accesAgenda(url = 'https://opendata-ajuntament.barcelona.cat/data/dataset/a25e60cd-3083-4252-9fce-81f733871cb1/resource/da9e71de-0f8e-417d-928a-56380bfd0231/download'):	
	# Realizar una solicitud HTTP GET para obtener el contenido del archivo JSON
	response = requests.get(url)
	
	# Verificar si la solicitud fue exitosa
	if response.status_code == 200:
		# Analizar el contenido JSON
		data = json.loads(response.text)
    
		# Ahora puedes acceder a los datos como un diccionario de Python
		agenda = pd.DataFrame(data)
        
        # Nos quedamos sólo con los eventos correspondientes al dia siguiente de hoy
		desiredDate = datetime.now() + timedelta(days=1)
		desiredDate = desiredDate.date()
        
        # Convertimos a fecha las fechas de los eventos del calendario
		agenda['start_date'] = pd.to_datetime(agenda['start_date'], utc=True)
		agenda['end_date'] = pd.to_datetime(agenda['end_date'], utc=True)
        
        # Nos quedamos exclusivamente los eventos del dia siguiente
		mask = agenda['start_date'].dt.date == desiredDate
        
        # Extraemos la información demografica del evento 
		infoDireccion = pd.DataFrame(agenda['addresses'].to_list())[0]
		iDic = pd.DataFrame(infoDireccion.tolist())
        
        # Agregamos la información dentro de la agenda
		agenda = pd.concat([agenda, iDic], axis = 1)

        # Devolvemos la agenda del dia
		return(agenda)

# ============================================================================= 
def normalitzaMatriuOverall(matrix):
    total_sum = sum(matrix)
    normalized_matrix = matrix / total_sum
    return normalized_matrix

# =============================================================================
def retornaFranja(string):
    print("string:")
    print(string)
    franjas = {
        "Morning": "01", "Afternoon":  "02", "Evening": "03", "LateEvening": "04",
        "Night": "05"}   
    return(franjas[string])

# =============================================================================
import numpy as np

def completar_matriz_ordenada(matriz_distancias, lista_estaciones):
    # Ordenar la lista de estaciones
    lista_estaciones = sorted(lista_estaciones)
    
    # Crear una lista de estaciones faltantes en las columnas
    estaciones_faltantes_columnas = list(set(lista_estaciones) - set(matriz_distancias.columns))
    
    # Crear una lista de estaciones faltantes en las filas
    estaciones_faltantes_filas = list(set(lista_estaciones) - set(matriz_distancias.index))
    
    # Agregar las estaciones faltantes en las columnas con valores de 0
    for estacion in estaciones_faltantes_columnas:
        matriz_distancias[estacion] = 0
    
    # Agregar las estaciones faltantes en las filas con valores de 0
    for estacion in estaciones_faltantes_filas:
        matriz_distancias.loc[estacion] = 0
    
    # Ordenar las filas y columnas de la matriz según la lista de estaciones
    matriz_distancias = matriz_distancias.reindex(columns=lista_estaciones, index=lista_estaciones)
    
    return matriz_distancias
    
    

# =============================================================================
#def guardarFichero(barrio, franja, respuesta, explicacion):
#        # Generamos el documento vacio
#        data = []
#        
#        # Para cada registro de la lista
#        for i in range(len(nombres)):
#            item = {
#                "neighbourhood": barrio[i],
#                "TimeZone": franja[i],
#                "response": respuesta[i],
#                "text": explicacion[i]}
#            data.append(item)
#
#        # Convertir la lista en formato JSON
#        json_data = json.dumps(data)
#        
#        return(json_data)
    
 # =============================================================================
def guardarFichero(barrio, franja, respuesta, explicacion):
        # Generamos el documento vacio
        data = []
        
        # Para cada registro de la lista
        item = {
            "neighbourhood": barrio,
            "TimeZone": franja,
            "response": respuesta,
            "text": explicacion}
        data.append(item)

        # Convertir la lista en formato JSON
        json_data = json.dumps(data)
        
        return(json_data)
