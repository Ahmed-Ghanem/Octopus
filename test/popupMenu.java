


import java.awt.Toolkit;

import javax.swing.*;
import java.awt.event.*;
import javax.swing.text.StyleContext;
/**
 * Show popup menus in sql editor
 * @author Ahmed Ghanem.
 */
public class popupMenu {

    private JPopupMenu menu1;
    private JPopupMenu menu2;
    private JTextPane textPane;
/***
 * show SQL commands [Generated]
 */
    public popupMenu() {


        menu1 = new JPopupMenu();
        menu2 = new JPopupMenu();


        JMenuItem menuItemadd = new JMenuItem("add");



        menuItemadd.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "add");



            }
        });



        menu1.add(menuItemadd);



        JMenuItem menuItemall = new JMenuItem("all");



        menuItemall.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "all");



            }
        });



        menu1.add(menuItemall);



        JMenuItem menuItemalter = new JMenuItem("alter");



        menuItemalter.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "alter");



            }
        });



        menu1.add(menuItemalter);



        JMenuItem menuItemand = new JMenuItem("and");



        menuItemand.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "and");



            }
        });



        menu1.add(menuItemand);



        JMenuItem menuItemany = new JMenuItem("any");



        menuItemany.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "any");



            }
        });



        menu1.add(menuItemany);



        JMenuItem menuItemas = new JMenuItem("as");



        menuItemas.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "as");



            }
        });



        menu1.add(menuItemas);



        JMenuItem menuItemasc = new JMenuItem("asc");



        menuItemasc.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "asc");



            }
        });



        menu1.add(menuItemasc);



        JMenuItem menuItembetween = new JMenuItem("between");



        menuItembetween.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "between");



            }
        });



        menu1.add(menuItembetween);



        JMenuItem menuItemby = new JMenuItem("by");



        menuItemby.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "by");



            }
        });



        menu1.add(menuItemby);



        JMenuItem menuItemchar = new JMenuItem("char");



        menuItemchar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "char");



            }
        });



        menu1.add(menuItemchar);



        JMenuItem menuItemcheck = new JMenuItem("check");



        menuItemcheck.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "check");



            }
        });



        menu1.add(menuItemcheck);



        JMenuItem menuItemcolumn = new JMenuItem("column");



        menuItemcolumn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "column");



            }
        });



        menu1.add(menuItemcolumn);



        JMenuItem menuItemcomment = new JMenuItem("comment");



        menuItemcomment.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "comment");



            }
        });



        menu1.add(menuItemcomment);



        JMenuItem menuItemconnect = new JMenuItem("connect");



        menuItemconnect.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "connect");



            }
        });



        menu1.add(menuItemconnect);



        JMenuItem menuItemcreate = new JMenuItem("create");



        menuItemcreate.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "create");



            }
        });



        menu1.add(menuItemcreate);



        JMenuItem menuItemdate = new JMenuItem("date");



        menuItemdate.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "date");



            }
        });



        menu1.add(menuItemdate);



        JMenuItem menuItemdecimal = new JMenuItem("decimal");



        menuItemdecimal.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "decimal");



            }
        });



        menu1.add(menuItemdecimal);



        JMenuItem menuItemdefault = new JMenuItem("default");



        menuItemdefault.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "default");



            }
        });



        menu1.add(menuItemdefault);



        JMenuItem menuItemdelete = new JMenuItem("delete");



        menuItemdelete.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "delete");



            }
        });



        menu1.add(menuItemdelete);



        JMenuItem menuItemdesc = new JMenuItem("desc");



        menuItemdesc.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "desc");



            }
        });



        menu1.add(menuItemdesc);



        JMenuItem menuItemdistinct = new JMenuItem("distinct");



        menuItemdistinct.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "distinct");



            }
        });



        menu1.add(menuItemdistinct);



        JMenuItem menuItemdrop = new JMenuItem("drop");



        menuItemdrop.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "drop");



            }
        });



        menu1.add(menuItemdrop);



        JMenuItem menuItemexclusive = new JMenuItem("exclusive");



        menuItemexclusive.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "exclusive");



            }
        });



        menu1.add(menuItemexclusive);



        JMenuItem menuItemexists = new JMenuItem("exists");



        menuItemexists.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "exists");



            }
        });



        menu1.add(menuItemexists);



        JMenuItem menuItemfloat = new JMenuItem("float");



        menuItemfloat.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "float");



            }
        });



        menu1.add(menuItemfloat);



        JMenuItem menuItemfrom = new JMenuItem("from");



        menuItemfrom.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "from");



            }
        });



        menu1.add(menuItemfrom);



        JMenuItem menuItemgrant = new JMenuItem("grant");



        menuItemgrant.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "grant");



            }
        });



        menu1.add(menuItemgrant);



        JMenuItem menuItemgroup = new JMenuItem("group");



        menuItemgroup.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "group");



            }
        });



        menu1.add(menuItemgroup);



        JMenuItem menuItemhaving = new JMenuItem("having");



        menuItemhaving.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "having");



            }
        });



        menu1.add(menuItemhaving);



        JMenuItem menuItemidentified = new JMenuItem("identified");



        menuItemidentified.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "identified");



            }
        });



        menu1.add(menuItemidentified);



        JMenuItem menuItemin = new JMenuItem("in");



        menuItemin.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "in");



            }
        });



        menu1.add(menuItemin);



        JMenuItem menuItemincrement = new JMenuItem("increment");



        menuItemincrement.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "increment");



            }
        });



        menu1.add(menuItemincrement);



        JMenuItem menuIteminsert = new JMenuItem("insert");



        menuIteminsert.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "insert");



            }
        });



        menu1.add(menuIteminsert);



        JMenuItem menuIteminteger = new JMenuItem("integer");



        menuIteminteger.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "integer");



            }
        });



        menu1.add(menuIteminteger);



        JMenuItem menuItemintersect = new JMenuItem("intersect");



        menuItemintersect.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "intersect");



            }
        });



        menu1.add(menuItemintersect);



        JMenuItem menuIteminto = new JMenuItem("into");



        menuIteminto.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "into");



            }
        });



        menu1.add(menuIteminto);



        JMenuItem menuItemis = new JMenuItem("is");



        menuItemis.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "is");



            }
        });



        menu1.add(menuItemis);



        JMenuItem menuItemlike = new JMenuItem("like");



        menuItemlike.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "like");



            }
        });
        menu1.add(menuItemlike);

        JMenuItem menuItemlock = new JMenuItem("lock");



        menuItemlock.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "lock");



            }
        });



        menu2.add(menuItemlock);



        JMenuItem menuItemlong = new JMenuItem("long");



        menuItemlong.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "long");



            }
        });



        menu2.add(menuItemlong);



        JMenuItem menuItemminus = new JMenuItem("minus");



        menuItemminus.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "minus");



            }
        });



        menu2.add(menuItemminus);



        JMenuItem menuItemmodify = new JMenuItem("modify");



        menuItemmodify.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "modify");



            }
        });



        menu2.add(menuItemmodify);



        JMenuItem menuItemnoaudit = new JMenuItem("noaudit");



        menuItemnoaudit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "noaudit");



            }
        });



        menu2.add(menuItemnoaudit);



        JMenuItem menuItemnocompress = new JMenuItem("nocompress");



        menuItemnocompress.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "nocompress");



            }
        });



        menu2.add(menuItemnocompress);



        JMenuItem menuItemnot = new JMenuItem("not");



        menuItemnot.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "not");



            }
        });



        menu2.add(menuItemnot);



        JMenuItem menuItemnowait = new JMenuItem("nowait");



        menuItemnowait.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "nowait");



            }
        });



        menu2.add(menuItemnowait);



        JMenuItem menuItemnull = new JMenuItem("null");



        menuItemnull.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "null");



            }
        });



        menu2.add(menuItemnull);



        JMenuItem menuItemnumber = new JMenuItem("number");



        menuItemnumber.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "number");



            }
        });



        menu2.add(menuItemnumber);



        JMenuItem menuItemof = new JMenuItem("of");



        menuItemof.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "of");



            }
        });



        menu2.add(menuItemof);



        JMenuItem menuItemon = new JMenuItem("on");



        menuItemon.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "on");



            }
        });



        menu2.add(menuItemon);



        JMenuItem menuItemonline = new JMenuItem("online");



        menuItemonline.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "online");



            }
        });



        menu2.add(menuItemonline);



        JMenuItem menuItemoption = new JMenuItem("option");



        menuItemoption.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "option");



            }
        });



        menu2.add(menuItemoption);



        JMenuItem menuItemor = new JMenuItem("or");



        menuItemor.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "or");



            }
        });



        menu2.add(menuItemor);



        JMenuItem menuItemorder = new JMenuItem("order");



        menuItemorder.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "order");



            }
        });



        menu2.add(menuItemorder);



        JMenuItem menuItempctfree = new JMenuItem("pctfree");



        menuItempctfree.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "pctfree");



            }
        });



        menu2.add(menuItempctfree);



        JMenuItem menuItemprior = new JMenuItem("prior");



        menuItemprior.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "prior");



            }
        });



        menu2.add(menuItemprior);



        JMenuItem menuItemprivileges = new JMenuItem("privileges");



        menuItemprivileges.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "privileges");



            }
        });



        menu2.add(menuItemprivileges);



        JMenuItem menuItempublic = new JMenuItem("public");



        menuItempublic.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "public");



            }
        });



        menu2.add(menuItempublic);



        JMenuItem menuItemraw = new JMenuItem("raw");



        menuItemraw.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "raw");



            }
        });



        menu2.add(menuItemraw);



        JMenuItem menuItemrename = new JMenuItem("rename");



        menuItemrename.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "rename");



            }
        });



        menu2.add(menuItemrename);



        JMenuItem menuItemrow = new JMenuItem("row");



        menuItemrow.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "row");



            }
        });



        menu2.add(menuItemrow);



        JMenuItem menuItemrownum = new JMenuItem("rownum");



        menuItemrownum.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "rownum");



            }
        });



        menu2.add(menuItemrownum);



        JMenuItem menuItemselect = new JMenuItem("select");



        menuItemselect.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "select");



            }
        });



        menu2.add(menuItemselect);



        JMenuItem menuItemset = new JMenuItem("set");



        menuItemset.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "set");



            }
        });



        menu2.add(menuItemset);



        JMenuItem menuItemsysdate = new JMenuItem("sysdate");



        menuItemsysdate.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "sysdate");



            }
        });



        menu2.add(menuItemsysdate);



        JMenuItem menuItemtable = new JMenuItem("table");



        menuItemtable.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "table");



            }
        });



        menu2.add(menuItemtable);



        JMenuItem menuItemto = new JMenuItem("to");



        menuItemto.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "to");



            }
        });



        menu2.add(menuItemto);



        JMenuItem menuItemunion = new JMenuItem("union");



        menuItemunion.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "union");



            }
        });



        menu2.add(menuItemunion);



        JMenuItem menuItemunique = new JMenuItem("unique");



        menuItemunique.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "unique");



            }
        });



        menu2.add(menuItemunique);



        JMenuItem menuItemupdate = new JMenuItem("update");



        menuItemupdate.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "update");



            }
        });



        menu2.add(menuItemupdate);



        JMenuItem menuItemuser = new JMenuItem("user");



        menuItemuser.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "user");



            }
        });



        menu2.add(menuItemuser);



        JMenuItem menuItemvalues = new JMenuItem("values");



        menuItemvalues.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "values");



            }
        });



        menu2.add(menuItemvalues);



        JMenuItem menuItemvarchar = new JMenuItem("varchar");



        menuItemvarchar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "varchar");



            }
        });



        menu2.add(menuItemvarchar);



        JMenuItem menuItemvarchar2 = new JMenuItem("varchar2");



        menuItemvarchar2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "varchar2");



            }
        });



        menu2.add(menuItemvarchar2);



        JMenuItem menuItemview = new JMenuItem("view");



        menuItemview.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "view");



            }
        });



        menu2.add(menuItemview);



        JMenuItem menuItemwhere = new JMenuItem("where");



        menuItemwhere.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {



                textPane.setText(textPane.getText() + "where");



            }
        });



        menu2.add(menuItemwhere);









    }

    public void show(JTextPane pane) {
        textPane = pane;
    }

    public void append(String s) { // better implementation--uses
        // StyleContext
        StyleContext sc = StyleContext.getDefaultStyleContext();
        int len = textPane.getDocument().getLength(); // same value as
        // getText().length();
        textPane.setCaretPosition(len); // place caret at the end (with no selection)
        textPane.replaceSelection(s); // there is no selection, so inserts at caret
    }

    public JPopupMenu getMenu1() {
        return menu1;
    }

    public JPopupMenu getMenu2() {
        return menu2;
    }
}
