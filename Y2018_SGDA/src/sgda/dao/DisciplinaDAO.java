package sgda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import sgda.model.ConnectionFactoryModel;
import sgda.model.DisciplinaModel;
import sgda.model.FormatarCamposModel;

public class DisciplinaDAO implements InterfaceDisciplinaDAO {

    private PreparedStatement stm = null;
    private ResultSet rs = null;
    private Connection con = null;

    @Override
    public List selectForCombo(String coluna) {

        try {
            List<String> listColuna = new ArrayList();

            con = ConnectionFactoryModel.getConnection();
            stm = con.prepareStatement("SELECT * FROM disciplina");
            rs = stm.executeQuery();

            while (rs.next()) {
                listColuna.add(rs.getString(coluna));
            }

            return listColuna;

        } catch (SQLException ex) {
            throw new RuntimeException("Exceção: " + ex);

        } finally {
            ConnectionFactoryModel.closeConnection(con, stm, rs);
        }
    }

    @Override
    public TableModel selectForTable() {

        try {
            con = ConnectionFactoryModel.getConnection();
            stm = con.prepareStatement("SELECT * FROM disciplina");
            rs = stm.executeQuery();

            return FormatarCamposModel.colocarDadosTabela(rs);

        } catch (SQLException ex) {
            throw new RuntimeException("Exceção: " + ex);

        } finally {
            ConnectionFactoryModel.closeConnection(con, stm, rs);
        }
    }
    
    @Override
    public TableModel selectForTable(String texto) {

        try {
            con = ConnectionFactoryModel.getConnection();
            stm = con.prepareStatement("SELECT * \n" +
            "FROM disciplina d \n" +
            "WHERE UPPER(d.nome_disciplina) LIKE UPPER('" + texto + "%')");
            rs = stm.executeQuery();

            return FormatarCamposModel.colocarDadosTabela(rs);

        } catch (SQLException ex) {
            throw new RuntimeException("Exceção: " + ex);

        } finally {
            ConnectionFactoryModel.closeConnection(con, stm, rs);
        }
    }

    @Override
    public void insert(DisciplinaModel ds) {

        try {
            con = ConnectionFactoryModel.getConnection();
            stm = con.prepareStatement("INSERT INTO disciplina (nome_disciplina, qnt_aulas, semestre, qnt_vagas, qnt_matriculado, situacao) VALUES (?, ?, ?, ?, ?, ?)");
            stm.setString(1, ds.getDescricao());
            stm.setInt(2, ds.getQntAulas());
            stm.setString(3, ds.getSemestre());
            stm.setInt(4, ds.getQntVagas());
            stm.setInt(5, ds.getQntMatriculado());
            stm.setString(6, ds.getSituacao());
            stm.executeUpdate();

            JOptionPane.showConfirmDialog(null, "Inserção feita com sucesso!", "SGDA - Aviso", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showConfirmDialog(null, "Houve algum erro durante a inserção!\n\nInformações técnicas sobre o erro: " + ex, "SGDA - Aviso", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

        } finally {
            ConnectionFactoryModel.closeConnection(con, stm, rs);
        }
    }

    @Override
    public void delete(DisciplinaModel ds) {

        try {
            con = ConnectionFactoryModel.getConnection();
            stm = con.prepareStatement("DELETE FROM disciplina WHERE cod_disciplina = ?");
            stm.setInt(1, ds.getCodDisciplina());
            stm.executeUpdate();

            JOptionPane.showConfirmDialog(null, "Remoção feita com sucesso!", "SGDA - Aviso", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showConfirmDialog(null, "Houve algum erro durante a inserção!\n\nInformações técnicas sobre o erro: " + ex, "SGDA - Aviso", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

        } finally {
            ConnectionFactoryModel.closeConnection(con, stm, rs);
        }
    }

    @Override
    public void update(DisciplinaModel ds) {

        try {
            con = ConnectionFactoryModel.getConnection();
            stm = con.prepareStatement("UPDATE disciplina SET nome_disciplina = ?, qnt_aulas = ?, semestre = ?, qnt_vagas = ?, situacao = ? WHERE cod_disciplina = ?");
            stm.setString(1, ds.getDescricao());
            stm.setInt(2, ds.getQntAulas());
            stm.setString(3, ds.getSemestre());
            stm.setInt(4, ds.getQntVagas());
            stm.setString(5, ds.getSituacao());
            stm.setInt(6, ds.getCodDisciplina());
            stm.executeUpdate();

            JOptionPane.showConfirmDialog(null, "Atualização feita com sucesso!", "SGDA - Aviso", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showConfirmDialog(null, "Houve algum erro durante a inserção!\n\nInformações técnicas sobre o erro: " + ex, "SGDA - Aviso", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

        } finally {
            ConnectionFactoryModel.closeConnection(con, stm, rs);
        }
    }
}
