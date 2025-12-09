import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class NetworkLabUI extends JFrame {

    private JComboBox<String> testCaseSelector;
    private JTextArea logArea;
    private GraphPanel graphPanel;
    private JComboBox<String> startStudentSelector;
    private JComboBox<String> targetCompanySelector;

    private List<List<UniversityStudent>> testCases;
    private List<UniversityStudent> currentStudents;
    private StudentGraph currentGraph;

    private boolean showingRoommates = false;
    private List<UniversityStudent> currentReferralPath = null;
    private UniversityStudent selectedNode = null; 

    private final Color BG_COLOR = new Color(43, 43, 43);
    private final Color PANEL_COLOR = new Color(60, 63, 65);
    private final Color TEXT_COLOR = new Color(230, 230, 230);
    private final Color ACCENT_COLOR = new Color(75, 110, 175);
    private final Color ACCENT_HOVER = new Color(95, 130, 195);
    private final Color SUCCESS_COLOR = new Color(98, 151, 85);
    private final Color SUCCESS_HOVER = new Color(118, 171, 105);
    private final Color PATH_COLOR = new Color(80, 200, 220);
    private final Color PATH_HOVER = new Color(100, 220, 240);
    private final Color EDGE_COLOR = new Color(180, 180, 180);
    private final Color SELECTION_COLOR = new Color(255, 215, 0); 

    private final Font MAIN_FONT = new Font("Segoe UI", Font.BOLD, 12);

    public NetworkLabUI() {
        super("University Network Visualizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 800);
        setLocationRelativeTo(null);
        
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        reloadTestCases();

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel controlPanel = createControlPanel();
        mainPanel.add(controlPanel, BorderLayout.NORTH);

        graphPanel = new GraphPanel();
        graphPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        mainPanel.add(graphPanel, BorderLayout.CENTER);

        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBackground(BG_COLOR);
        logPanel.setBorder(createStyledBorder("System Log"));
        logPanel.setPreferredSize(new Dimension(100, 150));
        
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logArea.setBackground(PANEL_COLOR);
        logArea.setForeground(TEXT_COLOR);
        logArea.setBorder(new EmptyBorder(5, 5, 5, 5));
        logPanel.add(new JScrollPane(logArea), BorderLayout.CENTER);
        
        mainPanel.add(logPanel, BorderLayout.SOUTH);

        add(mainPanel);

        logArea.setText("Welcome to the University Network Visualizer.\n");
        logArea.append("Select a Test Case and click 'Load & Run Case' to begin.");
    }

    private void reloadTestCases() {
        testCases = new ArrayList<>();
        testCases.add(Main.generateTestCase1());
        testCases.add(Main.generateTestCase2());
        testCases.add(Main.generateTestCase3());
    }

    private JPanel createControlPanel() {
        JPanel outerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        outerPanel.setBackground(BG_COLOR);

        JPanel datasetPanel = new JPanel(new BorderLayout(0, 12));
        datasetPanel.setBackground(PANEL_COLOR);
        datasetPanel.setBorder(new CompoundBorder(
                createStyledBorder("Dataset Controls"),
                new EmptyBorder(12, 15, 12, 15)));

        testCaseSelector = new JComboBox<>(new String[]{"Test Case 1", "Test Case 2", "Test Case 3"});
        testCaseSelector.setFont(MAIN_FONT);
        
        JButton masterRunButton = new JButton("Load & Run Case");
        styleButton(masterRunButton, ACCENT_COLOR, ACCENT_HOVER);
        masterRunButton.setPreferredSize(new Dimension(200, 40)); 
        masterRunButton.addActionListener(e -> {
            loadTestCase(testCaseSelector.getSelectedIndex());
            runAutomatedTests();
        });

        datasetPanel.add(testCaseSelector, BorderLayout.NORTH);
        datasetPanel.add(masterRunButton, BorderLayout.CENTER);

        JPanel actionsPanel = new JPanel(new GridBagLayout());
        actionsPanel.setBackground(PANEL_COLOR);
        actionsPanel.setBorder(new CompoundBorder(
                createStyledBorder("Network Actions"),
                new EmptyBorder(10, 15, 10, 15)));

        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2; 
        gbc.anchor = GridBagConstraints.CENTER; 
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 12, 0); 

        JButton roomButton = new JButton("Compute Roommates (Green)");
        styleButton(roomButton, SUCCESS_COLOR, SUCCESS_HOVER);
        roomButton.setPreferredSize(new Dimension(260, 32));
        roomButton.addActionListener(e -> toggleRoommateView());
        actionsPanel.add(roomButton, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 0);

        JPanel refFlow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        refFlow.setBackground(PANEL_COLOR);

        startStudentSelector = new JComboBox<>();
        startStudentSelector.setPreferredSize(new Dimension(110, 28));
        startStudentSelector.setFont(MAIN_FONT);
        
        targetCompanySelector = new JComboBox<>();
        targetCompanySelector.setPreferredSize(new Dimension(110, 28));
        targetCompanySelector.setFont(MAIN_FONT);
        
        JButton pathButton = new JButton("Find Path");
        styleButton(pathButton, PATH_COLOR, PATH_HOVER);
        pathButton.setForeground(new Color(30, 30, 30)); 
        pathButton.addActionListener(e -> findReferralPath());

        refFlow.add(createLabel("From:"));
        refFlow.add(startStudentSelector);
        refFlow.add(createLabel("To:"));
        refFlow.add(targetCompanySelector);
        refFlow.add(pathButton);

        actionsPanel.add(refFlow, gbc);

        outerPanel.add(datasetPanel);
        outerPanel.add(actionsPanel);

        return outerPanel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_COLOR);
        label.setFont(MAIN_FONT);
        return label;
    }

    private void styleButton(JButton btn, Color normalColor, Color hoverColor) {
        btn.setBackground(normalColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(MAIN_FONT);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(normalColor);
            }
        });
    }

    private TitledBorder createStyledBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), title);
        border.setTitleColor(TEXT_COLOR);
        border.setTitleFont(MAIN_FONT);
        return border;
    }

    private void loadTestCase(int index) {
        reloadTestCases();
        currentStudents = testCases.get(index);
        GaleShapley.assignRoommates(currentStudents);
        currentGraph = new StudentGraph(currentStudents);
        
        showingRoommates = false;
        currentReferralPath = null;
        selectedNode = null;
        
        startStudentSelector.removeAllItems();
        for (UniversityStudent s : currentStudents) {
            startStudentSelector.addItem(s.getName());
        }
        
        targetCompanySelector.removeAllItems();
        Set<String> uniqueCompanies = new HashSet<>();
        
        for (UniversityStudent s : currentStudents) {
            for (String intern : s.getPreviousInternships()) {
                if (intern != null && !intern.trim().isEmpty() && !intern.equalsIgnoreCase("None")) {
                    uniqueCompanies.add(intern);
                }
            }
        }
        
        List<String> sortedCompanies = new ArrayList<>(uniqueCompanies);
        Collections.sort(sortedCompanies);
        
        for (String company : sortedCompanies) {
            targetCompanySelector.addItem(company);
        }
        
        logArea.setText("Loaded Test Case " + (index + 1) + "\n");
        logArea.append("Students: " + currentStudents.size() + "\n");
        logArea.append("Roommates: Calculated (hidden).\n");
        logArea.append("Graph: Built with updated roommate weights.\n");
        graphPanel.repaint();
    }

    private void runAutomatedTests() {
        if (currentStudents == null) return;
        
        logArea.append("\n--- Running Grading Suite ---\n");
        int idx = testCaseSelector.getSelectedIndex();
        int score = Main.gradeLab(currentStudents, idx + 1);
        logArea.append("Test Score: " + score + "/100\n");
        logArea.append("Simulation Complete: Friends & Chats populated.\n");
        logArea.append("-----------------------------\n");
    }

    private void toggleRoommateView() {
        if (currentStudents == null) {
            logArea.append("\n⚠ Data not loaded. Click 'Load & Run Case'.\n");
            return;
        }
        
        showingRoommates = true;
        currentReferralPath = null;
        selectedNode = null;

        logArea.append("\n--- Displaying Roommate Pairs ---\n");
        
        for (UniversityStudent s : currentStudents) {
            if (s.getRoommate() != null) {
                if (s.getName().compareTo(s.getRoommate().getName()) < 0) {
                    logArea.append("✔ Pair: " + s.getName() + " <--> " + s.getRoommate().getName() + "\n");
                }
            } else if (!s.getRoommatePreferences().isEmpty()) {
                logArea.append("⚠ Unpaired: " + s.getName() + "\n");
            }
        }
        graphPanel.repaint();
    }

    private void findReferralPath() {
        if (currentGraph == null) {
            logArea.append("\n⚠ Data not loaded. Click 'Load & Run Case'.\n");
            return;
        }

        showingRoommates = false;
        selectedNode = null;
        String startName = (String) startStudentSelector.getSelectedItem();
        String targetCompany = (String) targetCompanySelector.getSelectedItem();
        
        if (startName == null) {
             logArea.append("\n⚠ Please select a starting student.\n");
             return;
        }
        
        if (targetCompany == null) {
            logArea.append("\n⚠ No internships available to search for in this group.\n");
            return;
       }

        UniversityStudent startNode = currentStudents.stream()
                .filter(s -> s.getName().equals(startName))
                .findFirst().orElse(null);

        if (startNode != null) {
            ReferralPathFinder finder = new ReferralPathFinder(currentGraph);
            currentReferralPath = finder.findReferralPath(startNode, targetCompany);
            
            logArea.append("\n--- Searching Referral Path ---\n");
            logArea.append("Target: " + targetCompany + "\n");
            
            if (currentReferralPath.isEmpty()) {
                logArea.append("No path found.\n");
            } else {
                StringBuilder sb = new StringBuilder("Path Found: ");
                for (int i = 0; i < currentReferralPath.size(); i++) {
                    sb.append(currentReferralPath.get(i).getName());
                    if (i < currentReferralPath.size() - 1) sb.append(" -> ");
                }
                logArea.append(sb.toString() + "\n");
            }
        } 
        graphPanel.repaint();
    }

    private void showStudentDetails(UniversityStudent s) {
        selectedNode = s;
        graphPanel.repaint(); 

        JDialog dialog = new JDialog(this, "Student Profile: " + s.getName(), true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(PANEL_COLOR);
        content.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel nameLabel = new JLabel(s.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        nameLabel.setForeground(ACCENT_COLOR);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(nameLabel);
        
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        content.add(new JSeparator());
        content.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel friendsHeader = new JLabel("Friend List");
        friendsHeader.setFont(MAIN_FONT);
        friendsHeader.setForeground(TEXT_COLOR);
        friendsHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(friendsHeader);

        StringBuilder friendsText = new StringBuilder();
        if (s.friends == null || s.friends.isEmpty()) {
            friendsText.append("None");
        } else {
            for (UniversityStudent f : s.friends) {
                friendsText.append("• ").append(f.getName()).append("\n");
            }
        }
        
        JTextArea friendsArea = new JTextArea(friendsText.toString());
        styleTextArea(friendsArea);
        content.add(friendsArea);

        content.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel chatHeader = new JLabel("Chat History");
        chatHeader.setFont(MAIN_FONT);
        chatHeader.setForeground(TEXT_COLOR);
        chatHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(chatHeader);

        StringBuilder chatText = new StringBuilder();
        if (s.chatHistory == null || s.chatHistory.isEmpty()) {
            chatText.append("None");
        } else {
            for (String msg : s.chatHistory) {
                chatText.append(msg).append("\n");
            }
        }

        JTextArea chatArea = new JTextArea(chatText.toString());
        styleTextArea(chatArea);
        
        JScrollPane chatScroll = new JScrollPane(chatArea);
        chatScroll.setBorder(null);
        chatScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(chatScroll);

        dialog.add(content);
        dialog.setVisible(true);
    }

    private void styleTextArea(JTextArea area) {
        area.setEditable(false);
        area.setBackground(PANEL_COLOR);
        area.setForeground(Color.LIGHT_GRAY);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.setAlignmentX(Component.LEFT_ALIGNMENT);
        area.setBorder(new EmptyBorder(5, 0, 5, 0));
    }

    private class GraphPanel extends JPanel {
        
        private Map<UniversityStudent, Point> nodePositions = new HashMap<>();

        public GraphPanel() {
            setBackground(new Color(50, 50, 50));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (currentStudents == null) return;
                    UniversityStudent clickedStudent = getStudentAt(e.getPoint());
                    if (clickedStudent != null) {
                        showStudentDetails(clickedStudent);
                    } else {
                        selectedNode = null;
                        repaint();
                    }
                }
            });
        }

        private UniversityStudent getStudentAt(Point p) {
            for (Map.Entry<UniversityStudent, Point> entry : nodePositions.entrySet()) {
                if (entry.getValue().distance(p) <= 20) { 
                    return entry.getKey();
                }
            }
            return null;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            if (currentStudents == null || currentGraph == null) {
                g.setColor(Color.GRAY);
                g.setFont(new Font("Segoe UI", Font.BOLD, 18));
                String msg = "Select a Test Case and press 'Load & Run Case' to start";
                FontMetrics fm = g.getFontMetrics();
                int msgWidth = fm.stringWidth(msg);
                g.drawString(msg, (getWidth() / 2) - (msgWidth / 2), getHeight() / 2);
                return;
            }

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int r = Math.min(width, height) / 3;
            int cx = width / 2;
            int cy = height / 2;

            nodePositions.clear();
            int n = currentStudents.size();
            for (int i = 0; i < n; i++) {
                double angle = 2 * Math.PI * i / n - Math.PI / 2; 
                int x = cx + (int) (r * Math.cos(angle));
                int y = cy + (int) (r * Math.sin(angle));
                Point p = new Point(x, y);
                nodePositions.put(currentStudents.get(i), p);
            }

            Color lineColor = (showingRoommates || currentReferralPath != null) 
                    ? new Color(80, 80, 80) 
                    : EDGE_COLOR;          
            
            g2.setStroke(new BasicStroke(2)); 

            for (UniversityStudent s : currentStudents) {
                for (StudentGraph.Edge e : currentGraph.getNeighbors(s)) {
                    UniversityStudent t = e.neighbor;
                    if (currentStudents.indexOf(t) <= currentStudents.indexOf(s)) continue;
                    
                    Point p1 = nodePositions.get(s);
                    Point p2 = nodePositions.get(t);
                    
                    g2.setColor(lineColor);
                    g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                    
                    if (!showingRoommates && currentReferralPath == null) {
                        int mx = (p1.x + p2.x) / 2;
                        int my = (p1.y + p2.y) / 2;
                        String weightStr = String.valueOf(e.weight);
                        
                        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                        FontMetrics fm = g2.getFontMetrics();
                        int textW = fm.stringWidth(weightStr);
                        int ascent = fm.getAscent();
                        
                        int diameter = Math.max(textW, ascent) + 12;
                        int radius = diameter / 2;

                        g2.setColor(new Color(50, 50, 50)); 
                        g2.fillOval(mx - radius, my - radius, diameter, diameter);
                        
                        g2.setColor(Color.WHITE);
                        g2.drawString(weightStr, mx - (textW / 2), my + (ascent / 3) + 1);
                    }
                }
            }

            if (showingRoommates) {
                g2.setColor(SUCCESS_COLOR);
                g2.setStroke(new BasicStroke(4));
                for (UniversityStudent s : currentStudents) {
                    if (s.getRoommate() != null) {
                        Point p1 = nodePositions.get(s);
                        Point p2 = nodePositions.get(s.getRoommate());
                        g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                    }
                }
            }

            if (currentReferralPath != null && !currentReferralPath.isEmpty()) {
                g2.setColor(PATH_COLOR);
                g2.setStroke(new BasicStroke(5));
                for (int i = 0; i < currentReferralPath.size() - 1; i++) {
                    UniversityStudent s1 = currentReferralPath.get(i);
                    UniversityStudent s2 = currentReferralPath.get(i + 1);
                    Point p1 = nodePositions.get(s1);
                    Point p2 = nodePositions.get(s2);
                    g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }

            for (UniversityStudent s : currentStudents) {
                Point p = nodePositions.get(s);
                
                Color nodeColor = ACCENT_COLOR; 
                if (showingRoommates && s.getRoommate() != null) nodeColor = SUCCESS_COLOR; 
                if (currentReferralPath != null && currentReferralPath.contains(s)) nodeColor = PATH_COLOR; 

                if (s == selectedNode) {
                    g2.setColor(SELECTION_COLOR);
                    g2.fillOval(p.x - 24, p.y - 24, 48, 48);
                }

                g2.setColor(nodeColor);
                g2.fillOval(p.x - 20, p.y - 20, 40, 40);
                
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(p.x - 20, p.y - 20, 40, 40);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(s.getName());
                g2.drawString(s.getName(), p.x - (textWidth / 2), p.y + 35);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NetworkLabUI().setVisible(true));
    }
}