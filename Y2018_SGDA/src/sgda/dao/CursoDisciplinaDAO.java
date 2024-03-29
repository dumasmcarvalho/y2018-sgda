package sgda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import sgda.model.ConnectionFactoryModel;
import sgda.model.CursoDisciplinaModel;
import sgda.model.FormatarCamposModel;

public class CursoDisciplinaDAO implements InterfaceCursoDisciplinaDAO {

    private PreparedStatement stm = null;
    private ResultSet rs = null;
    private Connection con = null;
    boolean duplicado = false;
    
    @Override
    public TableModel selectForTable() {
        
        try {
            con = ConnectionFactoryModel.getConnection();
            stm = con.prepareStatement("SELECT curso, nome_curso, disciplina, nome_disciplina \n" +
                "FROM curso_disciplina cd \n" +
                "JOIN curso c ON (c.cod_curso = cd.curso) \n" +
                "JOIN disciplina d ON (d.cod_disciplina = cd.disciplina) \n" +
                "ORDER BY nome_curso, nome_disciplina");
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
            stm = con.prepareStatement("SELECT curso, nome_curso, disciplina, nome_disciplina \n" +
            "FROM curso_disciplina cd \n" +
            "JOIN curso c ON (c.cod_curso = cd.curso) \n" +
            "JOIN disciplina d ON (d.cod_disciplina = cd.disciplina) \n" +
            "WHERE UPPER(nome_curso) LIKE UPPER('" + texto + "%') \n" +
            "ORDER BY nome_curso, nome_disciplina");
            rs = stm.executeQuery();

            return FormatarCamposModel.colocarDadosTabela(rs);

        } catch (SQLException ex) {
            throw new RuntimeException("Exceção: " + ex);

        } finally {
            ConnectionFactoryModel.closeConnection(con, stm, rs);
        }
    }

    @Override
    public void insert(CursoDisciplinaModel cd) {
                
        try {           
            con = ConnectionFactoryModel.getConnection();            
            stm = con.prepareStatement("SELECT * FROM curso_disciplina");
            rs = stm.executeQuery();
            
            while (rs.next()) {                
                if (cd.getCurso() == rs.getInt("curso") && cd.getDisciplina() == rs.getInt("disciplina")) {
                    duplicado = true;
                }                
            }
            
            stm = null;
            
            if (duplicado == false) {            
                stm = con.prepareStatement("INSERT INTO curso_disciplina (curso, disciplina) VALUES (?, ?)");
                stm.setInt(1, cd.getCurso());
                stm.setInt(2, cd.getDisciplina());
                stm.executeUpdate();

                JOptionPane.showConfirmDialog(null, "Inserção feita com sucesso!", "SGDA - Aviso", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showConfirmDialog(null, "Não foi possível realizar a inserção!\nMotivo: a relação informada já existe.", "SGDA - Aviso", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showConfirmDialog(null, "Houve algum erro durante a inserção!\n\nInformações técnicas sobre o erro: " + ex, "SGDA - Aviso", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

        } finally {
            ConnectionFactoryModel.closeConnection(con, stm, rs);
        }
    }

    @Override
    public void delete(CursoDisciplinaModel cd) {
        
        try {
            con = ConnectionFactoryModel.getConnection();
            stm = con.prepareStatement("DELETE FROM curso_disciplina WHERE curso = ? AND disciplina = ?");
            stm.setInt(1, cd.getCurso());
            stm.setInt(2, cd.getDisciplina());
            stm.executeUpdate();

            JOptionPane.showConfirmDialog(null, "Remoção feita com sucesso!", "SGDA - Aviso", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showConfirmDialog(null, "Houve algum erro durante a remoção!\n\nInformações técnicas sobre o erro: " + ex, "SGDA - Aviso", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

        } finally {
            ConnectionFactoryModel.closeConnection(con, stm, rs);
        }
    }

    @Override
    public void update(CursoDisciplinaModel cd, CursoDisciplinaModel cdu) {
        
        try {           
            con = ConnectionFactoryModel.getConnection();            
            stm = con.prepareStatement("SELECT * FROM curso_disciplina");
            rs = stm.executeQuery();
            
            while (rs.next()) {                
                if (cd.getCurso() == rs.getInt("curso") && cd.getDisciplina() == rs.getInt("disciplina")) {
                    duplicado = true;
                }                
            }
            
            stm = null;
            
            if (duplicado == false) {    
                stm = con.prepareStatement("UPDATE curso_disciplina SET curso = ?, disciplina = ? WHERE curso = ? AND disciplina = ?");
                stm.setInt(1, cd.getCurso());
                stm.setInt(2, cd.getDisciplina());
                stm.setInt(3, cdu.getCurso());
                stm.setInt(4, cdu.getDisciplina());
                stm.executeUpdate();

                JOptionPane.showConfirmDialog(null, "Atualização feita com sucesso!", "SGDA - Aviso", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showConfirmDialog(null, "Não foi possível realizar a atualização!\nMotivo: a relação informada já existe.", "SGDA - Aviso", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showConfirmDialog(null, "Houve algum erro durante a atualização!\n\nInformações técnicas sobre o erro: " + ex, "SGDA - Aviso", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);

        } finally {
            ConnectionFactoryModel.closeConnection(con, stm, rs);
        }
    }          
}
