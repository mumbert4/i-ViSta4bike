#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Tue Oct 31 17:36:59 2023

@author: ramitjans
"""

from sklearn.preprocessing import MinMaxScaler
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import networkx as nx
import re
import os

import datetime as dt



# =============================================================================
def getPlotsInInterval(matrizDistancias, lista_estaciones, tabla_estaciones, neighbourhood_name, time_zone, tipo = "day"):
    # print(f"Shape de la matriu d'adjacencies: {matrizDistancias.shape}")
    # print(f"Numero màxim de viatges entre dos estaciones dins del interval: {matrizDistancias.values.max()}")
    upper_triangular_mask = np.triu(np.ones(matrizDistancias.shape)).astype(bool)
    lower_triangular_mask = np.tril(np.ones(matrizDistancias.shape)).astype(bool)
    upper_adjacency = matrizDistancias.where(upper_triangular_mask)
    lower_adjacency = matrizDistancias.where(lower_triangular_mask)

    #Matriu d'adjacencies de la part de anada
    upper_adjacency_list = upper_adjacency.stack().reset_index()
    upper_adjacency_list.columns = ['Start Station','End Station','frequency']
    # print(f"Numero màxim de viatges d'anada {upper_adjacency_list['frequency'].max()}")
    upper_adjacency_list = upper_adjacency_list[upper_adjacency_list['frequency'] > 0]
    upper_adjacency_list = upper_adjacency_list[upper_adjacency_list['Start Station'] != upper_adjacency_list['End Station']]

    #Matriu d'adjacencies de la part de anada
    lower_adjacency_list = lower_adjacency.stack().reset_index()
    lower_adjacency_list.columns = ['Start Station','End Station','frequency']
    # print(f"Numero màxim de viatges de tornada {lower_adjacency_list['frequency'].max()}")
    lower_adjacency_list = lower_adjacency_list[lower_adjacency_list['frequency'] > 0]
    lower_adjacency_list = lower_adjacency_list[lower_adjacency_list['Start Station'] != lower_adjacency_list['End Station']]

    #Matriu d'adjacencies sumant el lower y el upper
    upper_np = np.nan_to_num(upper_adjacency.to_numpy())
    lower_np = np.nan_to_num(lower_adjacency.to_numpy())
    suma = upper_np + lower_np.T
    suma[np.diag_indices_from(suma)] = np.zeros(len(suma))

    dataUndirectedGraph = pd.DataFrame(suma, columns=upper_adjacency.columns, index=upper_adjacency.index)
    dataUndirectedGraph_list = dataUndirectedGraph.stack().reset_index()
    dataUndirectedGraph_list.columns = ['Start Station','End Station','frequency']
    # print(f"Numero màxim de viatges entre dos estacions {dataUndirectedGraph_list['frequency'].max()}")
    dataUndirectedGraph_list = dataUndirectedGraph_list[dataUndirectedGraph_list['frequency'] > 0]

    frecs_undirected = dataUndirectedGraph_list['frequency'].copy().values

    #Normalitzem entre 0 y 5 per tenir edges amb tamany adecuat
    normalizeFrequencyBetweenValues(dataUndirectedGraph_list, 5)

    #Generem edges amb weights
    undirectedEdges = list(zip(dataUndirectedGraph_list['Start Station'], dataUndirectedGraph_list['End Station'], dataUndirectedGraph_list['frequency']))

    pos = {}
    tabla_estaciones.index = tabla_estaciones['Station Id']
    for station in lista_estaciones:
        lat = tabla_estaciones.loc[tabla_estaciones['Station Id'] == station]['Latitude'].values[0]
        long = tabla_estaciones.loc[tabla_estaciones['Station Id'] == station]['Longitude'].values[0]
        if not isinstance(lat, np.float64):
            lat = float(lat.replace(',','.'))
        
        if not isinstance(long, np.float64):
            long = float(long.replace(',','.'))
        pos[station] = (long, lat)

    # createTripsUndirectedGraphReduced(tabla_estaciones, lista_estaciones, undirectedEdges, pos=pos, frecs=frecs_undirected,  title=f"Viatges entre dos estacions en el barri {neighbourhood_name}", barrio = neighbourhood_name, time_zone)
    createTripsUndirectedGraphReduced(tabla_estaciones, lista_estaciones, undirectedEdges, pos=pos, frecs=frecs_undirected,  title="", barrio = neighbourhood_name, time_zone = time_zone, tipo = tipo)


# =============================================================================
def createTripsUndirectedGraphReduced(data, selected_stations, edge_with_weights, pos, frecs, barrio, time_zone, tipo, figure_size=(8, 8), title = "Plot de viatges"):
    PATH = os.getcwd()

    
    OUTPUTDIR = PATH + "/../Output/"
    hoy = dt.date.today()
    
    CARPETAOUTPUT = OUTPUTDIR + hoy.strftime("%Y%m%d")

    offset = 0.001
    
    if data['Latitude'].dtype != "float64":
        data['Latitude'] = [float(v.replace(',', '.')) for v in data['Latitude']]
        
    if data['Longitude'].dtype != "float64":
        data['Longitude'] = [float(v.replace(',', '.')) for v in data['Longitude']]
    
    min_lat = data['Latitude'].min() - offset
    max_lat = data['Latitude'].max() + offset
    min_lon = data['Longitude'].min() - offset
    max_lon = data['Longitude'].max() + offset

    G = nx.Graph()
    G.add_nodes_from(selected_stations)
    G.add_weighted_edges_from(edge_with_weights)

    edges = G.edges()
    weights = [G[u][v]['weight'] for u,v in edges]
    fig, ax = plt.subplots(figsize=figure_size)
    nx.draw(G, pos=pos, width=weights, with_labels=True, node_color="tab:blue", edge_color=weights, edge_cmap=plt.cm.Reds, font_color="whitesmoke", node_size=500, ax=ax)

    ax.set_axis_on()
    plt.title(label=title)
    plt.xlabel(xlabel="Longitud")
    plt.ylabel(ylabel="Latitud")
    plt.ylim(min_lat, max_lat)
    plt.xlim(min_lon, max_lon)
    ax.tick_params(left=True, bottom=True, labelleft=True, labelbottom=True)

    vmin = min(frecs)
    vmax = max(frecs)

    cmap = plt.cm.Reds
    sm = plt.cm.ScalarMappable(cmap=cmap, norm=plt.Normalize(vmin=vmin, vmax=vmax))
    sm.set_array([])
    cbar = plt.colorbar(sm,ax=ax)
    
    # Guarda la figura como un archivo PNG
    match = re.search(r'\d+', barrio).group()
    if tipo == "day":
        plt.savefig(CARPETAOUTPUT+"/Graficos_day_" + str(match) + str(time_zone) + ".png", format="png", bbox_inches="tight")
    else:
        plt.savefig(CARPETAOUTPUT+"/Graficos_average_" + str(match) + str(time_zone) + ".png", format="png", bbox_inches="tight")

# =============================================================================    
def normalizeFrequencyBetweenValues(data, maximum_val):
    scaler = MinMaxScaler()
    data['frequency'] = scaler.fit_transform(data['frequency'].values.reshape(-1, 1))
    data['frequency']  = data['frequency'].apply(lambda x: x*maximum_val)

# =============================================================================
