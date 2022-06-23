package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.genes.model.Genes;
import it.polito.tdp.genes.model.Interactions;


public class GenesDao {
	
	public List<Genes> getAllGenes(){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				result.add(genes);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}
	
	public List<Integer> getCromosomi(){
		String sql="SELECT DISTINCT g.Chromosome as cromo "
				+ "FROM genes g "
				+ "WHERE g.Chromosome<>0 ";
		Connection conn = DBConnect.getConnection();
		List<Integer> result= new ArrayList<>();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				result.add(res.getInt("cromo"));
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}

	public List<Double> getPesoArco(int cromo1, int cromo2) {
		String sql="SELECT i.GeneID1 AS gene1, i.GeneID2 gene2, i.Expression_Corr AS num "
				+ "FROM interactions i "
				+ "WHERE i.GeneID1 IN (SELECT g.GeneID "
				+ "FROM genes g "
				+ "WHERE g.chromosome=? ) "
				+ "AND i.GeneID2 IN (SELECT g.GeneID "
				+ "FROM genes g "
				+ "WHERE g.chromosome=? ) ";
		Connection conn = DBConnect.getConnection();
		List<Double> parziali= new ArrayList<>();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, cromo1);
			st.setInt(2, cromo2);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				double r=res.getDouble("num");
				parziali.add(r);
			}
			res.close();
			st.close();
			conn.close();
			return parziali;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
		
	}
	
}
