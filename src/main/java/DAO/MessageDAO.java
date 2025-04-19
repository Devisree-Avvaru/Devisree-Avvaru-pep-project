package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.util.*;

import java.sql.*;

public class MessageDAO {

    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int messageId = rs.getInt(1);
                    return new Message(messageId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // for debugging
        }

        return null;
    }
    public Message deleteMessageById(int messageId) {
        Connection connection = ConnectionUtil.getConnection();
    
        try {
            // First, retrieve the message to return it later
            String selectSql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement selectStmt = connection.prepareStatement(selectSql);
            selectStmt.setInt(1, messageId);
            ResultSet rs = selectStmt.executeQuery();
    
            if (rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
    
                // Now delete the message
                String deleteSql = "DELETE FROM message WHERE message_id = ?";
                PreparedStatement deleteStmt = connection.prepareStatement(deleteSql);
                deleteStmt.setInt(1, messageId);
                deleteStmt.executeUpdate();
    
                return message;
            }
    
        } catch (SQLException e) {
            e.printStackTrace(); // Optional: logging
        }
    
        return null;
    }
    public List<Message> getAllMessagesByUserId(int userId){
        List<Message> messages = new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                messages.add(message);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return messages;
    } 
    public List<Message> getAllMessages(){
        List<Message> messages=new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM message";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message message=new Message(
                rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
                messages.add(message);
            }

        }catch(SQLException e){
            e.printStackTrace();

        }
        return messages;
    }
    public Message getMessageByMessageId(int messageId){
        Connection conn = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, messageId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return new Message(
                rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
            }

        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public Message updateMessageText(int messageId, String newText) {
        try (Connection conn = ConnectionUtil.getConnection()) {
            // checking message exists
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, messageId);
            ResultSet rs = ps.executeQuery();
    
            if (rs.next()) {
                String updateSql = "UPDATE message SET message_text = ? WHERE message_id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setString(1, newText);
                updateStmt.setInt(2, messageId);
                updateStmt.executeUpdate();
    
                // Return updated message
                return new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        newText,
                        rs.getLong("time_posted_epoch")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}