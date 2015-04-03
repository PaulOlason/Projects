// Paul Olason, CSCI 367, Summer 2014, GCT Project
//
// The GCT is a simple chat program that allows users to
// connect to one another over a local network. Users are able
// to send messages to all other users, or specifically those within
// their group. Messages are passed circularly, and the links
// between users are modified to maintain the circle as users are added
// and dropped.

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.border.EmptyBorder;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;



public class GCT_Final {
   private static String user_id;           
   private static String group_id;
   private static String neighbor_id;        // tracks id of nearest neighbor
   private static String neighbor_group;     // tracks group of nearest neighbor
   private static InetAddress ip;            // address of user
   private static InetAddress neighbor1;     // address of nearest neighbor
   private static InetAddress neighbor2;     // address of next consecutive neighbor
   private static int port = 48159;          // port used for all connections
   private static ServerSocket user;         // socket for incoming communication
   private static Socket connection;         // socket for outgoing communication
   private static JTextArea groupDisplay;    // displays all group messages
   private static JTextArea generalDisplay;  // displays all general messages
   private static messageThread msgthread;   // receives incoming messages
   
   
   
   public static void main(String[] args) {
      login();
   }
   
   // Builds the login window, sets the user and group names
   public static void login() {
      final JFrame startUp = new JFrame("Welcome to GCT");
      startUp.setResizable(false);
      startUp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      startUp.setLocationByPlatform(true);
      
      // creates a panel to hold the login button
      JPanel buttonPanel = new JPanel();
      buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
      buttonPanel.setBorder(new EmptyBorder(new Insets(10, 100, 10, 100)));
      JButton login = new JButton("Login");
      buttonPanel.add(Box.createHorizontalGlue());
      
      // creates a panel that holds the input for the user's information
      JPanel textPanel = new JPanel();
      textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
      textPanel.setBorder(new EmptyBorder(new Insets(60, 50, 60, 50)));
      JLabel userId = new JLabel("User Name:");
      JLabel groupId = new JLabel("Group Name:");
      final JTextField userInput = new JTextField(15);
      final JTextField groupInput = new JTextField(15);
      textPanel.add(userId);
      textPanel.add(userInput);
      textPanel.add(groupId);
      textPanel.add(groupInput);
      
      // listens for when login is pressed, stores user information
      login.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            user_id = userInput.getText();
            group_id = groupInput.getText();
            startUp.dispose();
            join();
            
            
         }
      });
      
      buttonPanel.add(login);
      buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
      Container contentPane = startUp.getContentPane();
      contentPane.add(textPanel, BorderLayout.PAGE_START);
      contentPane.add(buttonPanel, BorderLayout.PAGE_END);
      startUp.pack();
      startUp.setVisible(true);
      
   }
   
   // Searches the network for other users, and joins them if any are available
   public static void join() {
      try {
         
         user = new ServerSocket(port);
         int port = 48159;
         boolean roomFound = false;
         Enumeration<NetworkInterface> netList = NetworkInterface.getNetworkInterfaces();
         NetworkInterface network = Collections.list(netList).get(0);
         if (!roomFound) {
            Enumeration<InetAddress> addresses = network.getInetAddresses();
            InetAddress address = Collections.list(addresses).get(1);
            ip = address;
            byte[] split = address.getAddress();
            
            // iterates over all possible ip values in the local network
            for (int i = 1; i < 257; i++) {
               if (i == 203) {
                  System.out.println();
               }
               System.out.println(i);
               split[3] = (byte)i;
               InetAddress current = InetAddress.getByAddress(split);
               if (current.isReachable(100) && !ip.getAddress().equals(current)) {
                  try {
                     connection = new Socket(current, port);
                     ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
                     
                     // request's found user's information, user_id and group_id
                     String[] request = {"START", user_id, group_id, ip.getHostAddress()};
                     out.writeObject(request);
                     Socket reciever = user.accept();
                     out.close();
                     ObjectInputStream in = new ObjectInputStream(reciever.getInputStream());
                     String[] response = (String[])in.readObject();
                     in.close();
                     connection.close();
                     if (!response[0].equals("START")) {
                        neighbor_id = response[1];
                        neighbor_group = response[2];
                        neighbor1 = InetAddress.getByName(response[3]);
                        neighbor2 = InetAddress.getByName(response[4]);
                        roomFound = true;
                        break;
                     }
                     
                  }catch (ClassNotFoundException e) {
                     System.out.println(e);
                  }catch (IOException e) {
                  }
                  
               }
            }
         }
         
         // connects the user to themselves if no one else is in the network
         if (!roomFound) {
            neighbor1 = ip;
            neighbor2 = ip;
            neighbor_id = user_id;
            neighbor_group = group_id;
         }
         if (true) {
            ChatThread chat = new ChatThread();
            chat.start();
         }
         connection = new Socket(neighbor1, port);
         ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
         
         // delivers login message
         String[] loginMsg = {"IN", user_id, group_id, ip.getHostName(), neighbor1.getHostName()};
         out.writeObject(loginMsg);
         out.close();
         connection.close();
      } catch(UnknownHostException e) {
         System.out.println(e);
      } catch (IOException e) {
         System.out.println(e);
         
      }
   }
   
   
   // Runs the general chat functions. Takes in user input and transfers
   // to other methods. Logs user out when window is closed
   public static class ChatThread extends Thread {
      public void run() {
         final JFrame chat = new JFrame("GCT - " + user_id + " - " + group_id);
         chat.setResizable(false);
         chat.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
         
         // logs the user out when the window is closed
         chat.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
               chat.setVisible(false);
               chat.dispose();
               logOut();
               System.exit(1);
            }
         });
         
         // creates title label for each chat
         JPanel title = new JPanel();
         title.setBorder(new EmptyBorder(new Insets(6, 10, 6, 5)));
         title.setLayout(new BoxLayout(title, BoxLayout.X_AXIS));       
         JLabel groupHeader = new JLabel("Group Communication");
         JLabel generalHeader = new JLabel("General Communication");
         title.add(groupHeader);
         title.add(Box.createRigidArea(new Dimension(110, 0)));
         title.add(generalHeader);
         chat.add(title);
         chat.pack();
         
         // creates the group chat interface
         JPanel group = new JPanel();
         group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));
         group.setBorder(new EmptyBorder(new Insets(30, 10, 5, 10)));
         final JTextField groupMessage = new JTextField(15);
         groupDisplay = new JTextArea();
         groupDisplay.setColumns(20);
         groupDisplay.setRows(20);
         groupDisplay.setLineWrap(true);
         groupDisplay.setEditable(false);
         groupDisplay.setWrapStyleWord(true);
         JScrollPane scrollGrp = new JScrollPane(groupDisplay);
         scrollGrp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
         JButton sendGroup = new JButton("Send");
         group.add(scrollGrp);
         group.add(groupMessage);
         group.add(sendGroup);
         chat.add(group);
         chat.pack();
         
         // takes user input and delivers a group message
         sendGroup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               String msg = groupMessage.getText();
               groupMessage.setText("");
               sendMessage("GRP", msg);
            }
         });
         
         // creates the general chat interface
         JPanel general = new JPanel();
         general.setLayout(new BoxLayout(general, BoxLayout.Y_AXIS));
         general.setBorder(new EmptyBorder(new Insets(30, 275, 5, 10)));
         final JTextField generalMessage = new JTextField(15);
         generalDisplay = new JTextArea();
         generalDisplay.setColumns(20);
         generalDisplay.setRows(20);
         generalDisplay.setLineWrap(true);
         generalDisplay.setWrapStyleWord(true);
         generalDisplay.setEditable(false);
         JScrollPane scrollGen = new JScrollPane(generalDisplay);
         scrollGen.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);       
         JButton sendGeneral = new JButton("Send");
         general.add(scrollGen);
         general.add(generalMessage);
         general.add(sendGeneral);        
         chat.add(general);
         chat.pack();
         
         // takes user input and delivers a general message
         sendGeneral.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               String msg = generalMessage.getText();
               generalMessage.setText("");
               sendMessage("MSG", msg);
            }
         });
         chat.setVisible(true);
         
         msgthread = new messageThread();
         msgthread.on(true);
         msgthread.run();
         
      }
   }
   
   // Accepts connections from other users and interprets their messages
   public static class messageThread extends Thread {
      boolean on;
      
      // Used to stop thread
      public void on(boolean set) {
         on = set;
      }
      
      // Takes in messages from the server socket and identifies appropriate action
      // Delivers message to next user as necessary
      public void run() {
         while(on) {
            try {
               System.out.println(neighbor1);
               System.out.println(neighbor2);
               Socket input = user.accept();
               ObjectInputStream in = new ObjectInputStream(input.getInputStream());
               Object obj = in.readObject();
               String[] msg = (String[])obj;
               String type = msg[0];
               System.out.println(neighbor_id);
               System.out.println(neighbor_group);
               // standard general message
               if (type.equals("MSG")) {
                  String test = msg[3];
                  String message = msg[1] + ", " + msg[2] + ": " + msg[3];
                  generalDisplay.append(message + "\n");
               // standard group message  
               }else if (type.equals("GRP")) {
                  if (group_id.equals(msg[2])) {
                     String message = msg[1] + ", " + msg[2] + ": " + msg[3];
                     groupDisplay.append(message + "\n");
                  }
               // sent during login, receiver returns their neighbor information to the client
               }else if (type.equals("START")) {
                  connection = new Socket(InetAddress.getByName(msg[3]), port);
                  ObjectOutputStream back = new ObjectOutputStream(connection.getOutputStream());
                  String[] response = {"RESP", user_id, group_id, ip.getHostName(), neighbor1.getHostName()};
                  back.writeObject(response);
                  back.close();
                  connection.close();
               // login message, updates neighbors when necessary
               }else if (type.equals("IN")) {
                  generalDisplay.append(msg[1] + " has joined the chat room.\n");
                  if (msg[2].equals(group_id)) {
                     groupDisplay.append(msg[1] + " has joined the chat room.\n");
                  }
                  InetAddress newUser = InetAddress.getByName(msg[3]);
                  InetAddress dispacedNeighbor = InetAddress.getByName(msg[4]);
                  if (dispacedNeighbor.equals(neighbor1) && !newUser.equals(ip)) {
                     neighbor2 = neighbor1;
                     neighbor1 = newUser;
                     neighbor_id = msg[1];
                     neighbor_group = msg[2];
                  }else if (dispacedNeighbor.equals(neighbor2)) {
                     neighbor2 = newUser;
                  }
               // Log out message, updates neighbors when necessary
               }else if (type.equals("OUT")) {
                  generalDisplay.append(msg[6] + " has left the chat room.\n");
                  if (group_id.equals(msg[7])) {
                     groupDisplay.append(msg[6] + " has left the chat room.\n");
                  }
                  InetAddress leavingUser = InetAddress.getByName(msg[3]);
                  if (leavingUser.equals(neighbor1)) {
                     InetAddress dispacedNeighbor2 = InetAddress.getByName(msg[5]);
                     neighbor1 = neighbor2;
                     neighbor2 = dispacedNeighbor2;
                     neighbor_id = msg[1];
                     neighbor_group = msg[2];
                  }else if (leavingUser.equals(neighbor2)) {
                     InetAddress dispacedNeighbor1 = InetAddress.getByName(msg[4]);
                     neighbor2 = dispacedNeighbor1;
                  }
               // sent to the next available user when a client logs off or a user is found missing
               // receiver delivers log out message to avoid issues with missing connections
               }else if (type.equals("LOST")) {
                  connect(true);
                  String[] alert = {"OUT", user_id, group_id, msg[3], ip.getHostName(), neighbor1.getHostName(), msg[1], msg[2]};
                  ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
                  out.writeObject(alert);
                  out.close();
               }
               if (!type.equals("START") && !type.equals("LOST") && !type.equals("GRP_START") && !user_id.equals(msg[1])) {
                  connect(false);
                  ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
                  out.writeObject(msg);
                  out.close();
               }
               input.close();
               in.close();
            }catch (ClassNotFoundException e) {
               System.out.println(e);
               
            } catch (IOException e) {
               
            }
            
         }
      }
   }
   
   // Attempts to connect to neighbors and handles when neighbors cannot be reached
   // lost indicates whether or not the connection has already been found missing
   public static void connect(boolean lost) {
      try {
         connection = new Socket(neighbor1, port);
         connection.setSoTimeout(5000);
      }catch (IOException a) {
         try {
            connection = new Socket(neighbor2, port);
            connection.setSoTimeout(5000);
            if (!lost) {
               generalDisplay.append("Error sending message. Please resend.\n");
               if (group_id.equals(neighbor_group)) {
                  groupDisplay.append("Error sending message. Please resend.\n");
               }
               String[] msg = {"LOST", neighbor_id, neighbor_group, neighbor1.getHostName()};
               ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
               out.writeObject(msg);
               out.close();
            }
         }catch (IOException b) {
            // neither connection available, user logs out and reconnects to the network
            generalDisplay.append("Connection error. Please resend.\n");
            logOut();
            join();
            connect(false);
         }
      }
   }
   
   // Creates a message from the given message string and delivers it
   // to an available connection
   public static void sendMessage(String type, String message) {
      try {
         connect(false);
         String[] msg = {type, user_id, group_id, message};
         ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
         out.writeObject(msg);
         out.close();
         connection.close();
      }catch (IOException e) {
         System.out.println(e);
      }
      
   }
   
   // Delivers the log out message to the next user
   public static void logOut() {
      try {
         String[] msg = {"LOST", user_id, group_id, ip.getHostName()};
         connect(false);
         ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
         out.writeObject(msg);
         msgthread.on(false);
         connection.close();
         user.close();
      }catch (IOException e) {
         System.out.println(e);
      }
      
   }   
}