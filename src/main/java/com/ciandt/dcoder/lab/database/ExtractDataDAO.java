package com.ciandt.dcoder.lab.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ciandt.dcoder.lab.CardAPILab;
import com.ciandt.dcoder.lab.model.Card;

public class ExtractDataDAO {

	private Connection connection;

	public ExtractDataDAO() {
		this.connection = new ConnectionFactory().getConnection();
	}

	public List<Categoria> getTermometro() {
		try {
			List<Categoria> cards = new ArrayList<Categoria>();

			String sql = "SELECT count(*) valor, c.Name titulo,  (select dc.[Description] from dcoder_Category dc where dc.[Name] = c.[Name]) description  "
					+ " FROM dcoder_POST_v2 P"
					+ " JOIN dcoder_PredictionLabel l on p.PredictionLabelResult = l.Label"
					+ " JOIN dcoder_Category c on l.CategoryId = c.Id "
					+ " where p.PostDate between ('2014-01-01') and ('2015-12-30')  Group by c.Name";

			PreparedStatement st = this.connection.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Categoria c = new Categoria();
				c.setTitulo(rs.getString("titulo"));
				c.setValor(rs.getLong("valor"));
				c.setDescricao(rs.getString("description"));
				cards.add(c);
			}
			return cards;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	public List<Categoria> getRadar() {

		try {
			List<Categoria> cards = new ArrayList<Categoria>();
			String sql = "select p.PredictionLabelResult titulo, count(*) valor"
					+ " from [Dcoder_Post_v2] p where p.PostDate between ('2014-01-01') and ('2015-12-30') "
					+ " and PredictionLabelResult is not null group by p.PredictionLabelResult order by 2 desc";

			PreparedStatement st = this.connection.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Categoria c = new Categoria();
				c.setTitulo(rs.getString("titulo"));
				c.setValor(rs.getLong("valor"));
				cards.add(c);
			}
			return cards;
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}

}
