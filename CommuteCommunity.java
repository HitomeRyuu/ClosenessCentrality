package com.swt.segment.common.testComu;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CommuteCommunity {
    private int nVertices;
    private int nEdges;
    private Map<Integer, Set<Integer>> adjList;
    private Map<Integer, CommunityVO> communityMap;
    private Map<Integer, Integer> vertexToCommunity;
    private ArrayList<Integer> verticesID;
    private Map res = new HashMap();


    public CommuteCommunity(String filename){
        nVertices = 0;
        nEdges = 0;
        adjList = new TreeMap<>();
        communityMap = new TreeMap<>();
        vertexToCommunity = new TreeMap<>();
        Set<Integer> setVerticesID = new TreeSet<>();
        Scanner sc = null;
        try {
            sc = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (sc.hasNextLine()){
            nEdges++;
            String line = sc.nextLine();
            String edge[] = line.split(" ");
            int vertex_1 = Integer.parseInt(edge[0]);
            int vertex_2 = Integer.parseInt(edge[1]);
            setVerticesID.add(vertex_1);
            setVerticesID.add(vertex_2);
            if(adjList.containsKey(vertex_1)){
                adjList.get(vertex_1).add(vertex_2);
            }else {
                Set<Integer> set = new TreeSet<>();
                set.add(vertex_2);
                adjList.put(vertex_1, set);
            }

            if(adjList.containsKey(vertex_2)){
                adjList.get(vertex_2).add(vertex_1);
            }else {
                Set<Integer> set = new TreeSet<>();
                set.add(vertex_1);
                adjList.put(vertex_2, set);
            }

        }
        nVertices = adjList.keySet().size();
        verticesID = new ArrayList<>(setVerticesID);
        for(int i=0;i< nVertices; i++){
            Set<Integer> set = new TreeSet<>();
            set.add(verticesID.get(i));
            int tot;
            tot = adjList.get(verticesID.get(i)).size();
            CommunityVO communityVO = new CommunityVO(i, set, 0, tot);
            communityMap.put(i, communityVO);
            vertexToCommunity.put(verticesID.get(i),i);
        }
    }

    public double calculateModularity(){
        double q = 0;
        int m2 = nEdges;
        Iterator<CommunityVO> communityIterator = communityMap.values().iterator();
        while (communityIterator.hasNext()) {
            CommunityVO comm = communityIterator.next();
            double in = (double) comm.getIn();
            double tot = (double) comm.getTot();
            double t = (in / m2 - (tot / (m2 * 2)) * (tot / (m2 * 2)));
            q += t;
        }
        return q;
    }

    public ArrayList<Integer> communityNeighborToVertex(int vertexID){
        ArrayList<Integer> communityArrayList = new ArrayList<Integer>();
        Set<Integer> vertexNeighborToThisVertex = adjList.get(vertexID);
        for(Integer vertex: vertexNeighborToThisVertex){
            communityArrayList.add(vertexToCommunity.get(vertex));
        }
        return  communityArrayList;
    }

    public int distanceFromVertexToCommunity(int vertexID, int commID){
        Set<Integer> vertexInThisCommunity = communityMap.get(commID).getVertices();
        Set<Integer> adjListOfThisVertex = adjList.get(vertexID);
        Set<Integer> mutual = new TreeSet<>(adjListOfThisVertex);
        mutual.retainAll(vertexInThisCommunity);
        return mutual.size();
    }


    public void remove(int commID, int vertexID) {
        communityMap.get(commID).setTot(communityMap.get(commID).getTot() - adjList.get(vertexID).size());
        communityMap.get(commID).setIn(communityMap.get(commID).getIn() - distanceFromVertexToCommunity(vertexID, commID));
        communityMap.get(commID).getVertices().remove(vertexID);
        if (communityMap.get(commID).getVertices().size() == 0) {
            communityMap.remove(commID);
        }
        vertexToCommunity.replace(vertexID, -1);
    }

    public void insert(int commID, int vertexID) {
        communityMap.get(commID).setTot(communityMap.get(commID).getTot() + adjList.get(vertexID).size());
        communityMap.get(commID).setIn(communityMap.get(commID).getIn() + distanceFromVertexToCommunity(vertexID, commID));
        communityMap.get(commID).getVertices().add(new Integer(vertexID));
        vertexToCommunity.replace(vertexID, commID);
    }

    public double modularityGain(int vertexID, int commID) {
        double m2 = (double) nEdges * 2;
        double tot = (double) communityMap.get(commID).getTot();
        double in = (double) communityMap.get(commID).getIn();
        double ki = (double) adjList.get(vertexID).size();
        double kiin = (double) distanceFromVertexToCommunity(vertexID, commID);
        double delta = ((in + 2 * kiin) / m2 - ((tot + ki) / m2) * ((tot + ki) / m2)) - (in / m2 - (tot / m2) * (tot / m2) - (ki / m2) * (ki / m2));
        return delta;
    }

    public Map optimize(){
        double maxModularity = Double.MIN_VALUE;
        while (true){
            for(int i=0;i<nVertices;i++){
                double maxGain = 0;
                ArrayList<Integer> communityNeighborToVertex = communityNeighborToVertex(verticesID.get(i));
                double gain =0;
                int targetCommunity = 0;
                for (Integer comm: communityNeighborToVertex){
                    gain = modularityGain(verticesID.get(i), comm);
                    if(gain>maxGain){
                        maxGain = gain;
                        targetCommunity = comm;
                    }
                }
                if(maxGain ==0){
                    continue;
                }
                if(maxGain>0){
                    remove(vertexToCommunity.get(verticesID.get(i)),verticesID.get(i));
                    insert(targetCommunity, verticesID.get(i));
                }
            }
            double currentModularity = calculateModularity();
            //System.out.println("Current Modularity: " + currentModularity);
           // System.out.println("Max Modularity: " + maxModularity);
            //System.out.println();
            if(maxModularity == currentModularity){

                int i = 0;
                for (CommunityVO comm : communityMap.values()) {
                    Set<Integer> vertices = comm.getVertices();
                    res.put(i,vertices);
                    i++;
                    //System.out.println(vertices);
                }
                res.put("size",communityMap.size());
                break;
            }
            if( maxModularity < currentModularity){
                maxModularity = currentModularity;
            }

        }
        return res;
    }

}

//0.8103872191636612