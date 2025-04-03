
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FrmAppidiomas extends JFrame {
    JButton btnTraducir;
    JComboBox<String> cmbFrases;
    JComboBox<String> cmbIdiomas;
    JTextField txtFrasestraducciones;
    List<String> idiomasUnicos = new ArrayList<>();
    List<String> frasesUnicas = new ArrayList<>();
    FrasesData data = new FrasesData();
    String selecFrase;
    String selecIdioma;

    public FrmAppidiomas() {
        setSize(600, 300);
        setTitle("Aplicacion de idiomas");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblFrase = new JLabel("Frase");
        lblFrase.setBounds(10, 10, 100, 25);
        getContentPane().add(lblFrase);

        JLabel lblIdioma = new JLabel("Idioma");
        lblIdioma.setBounds(10, 50, 100, 25);
        getContentPane().add(lblIdioma);

        String nombreArchivo = System.getProperty("user.dir")
                + "/src/datos/FrasesTraducidas.json";
        // Cargar JSON desde archivo

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            data = objectMapper.readValue(new File(nombreArchivo), FrasesData.class);
            System.out.println(data);
            // Obtener lista única de idiomas
            idiomasUnicos = data.getFrases().stream()
                    .flatMap(frase -> frase.gettraducciones().stream())
                    .map(Traduccion::getidioma)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
            System.out.println(idiomasUnicos);

            frasesUnicas = data.getFrases().stream()
                    .map(Frase::getTexto)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
            System.out.println(frasesUnicas);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No se pudieron cargar los datos: " + ex);
        }
        cmbFrases = new JComboBox<>();
        cmbFrases.setBounds(80, 10, 150, 25);
        for (String texto : frasesUnicas) {
            cmbFrases.addItem(texto);
        }
        cmbFrases.insertItemAt("Selecciona la frase", 0);
        cmbFrases.setSelectedIndex(0);
        getContentPane().add(cmbFrases);

        cmbIdiomas = new JComboBox<>();
        cmbIdiomas.setBounds(80, 50, 150, 25);
        for (String idioma : idiomasUnicos) {
            cmbIdiomas.addItem(idioma);
        }
        cmbIdiomas.insertItemAt("Selecciona el idioma", 0);
        cmbIdiomas.setSelectedIndex(0);
        getContentPane().add(cmbIdiomas);

        btnTraducir = new JButton("Traducir");
        btnTraducir.setBounds(10, 90, 100, 25);
        getContentPane().add(btnTraducir);

        txtFrasestraducciones = new JTextField();
        txtFrasestraducciones.setBounds(10, 140, 400, 70);
        getContentPane().add(txtFrasestraducciones);

        JButton btnPlay = new JButton();
        btnPlay.setIcon(new ImageIcon(getClass().getResource("/imagenes/play.png")));
        btnPlay.setToolTipText("Reproducir");
        btnPlay.setBounds(450, 140, 70, 70);
        getContentPane().add(btnPlay);

        btnTraducir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Traducir();
                cmbFrases.setSelectedIndex(0);
                cmbIdiomas.setSelectedIndex(0);

            }
        });
        btnPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Play();

            }
        });

    }

    private void Traducir() {

        selecFrase = (String) cmbFrases.getSelectedItem();
        selecIdioma = (String) cmbIdiomas.getSelectedItem();
        if (selecFrase == "Selecciona la frase" || selecIdioma == "Selecciona el idioma") {
            JOptionPane.showMessageDialog(null, "Debes seleccionar una frase y un idioma para poder traducir.");
            return; // Salir del método si no hay selección
        }
        boolean traduccionEncontrada = false;
        for (Frase frase : data.getFrases()) {
            if (frase.getTexto().equals(selecFrase)) {
                for (Traduccion traduccion : frase.gettraducciones()) {
                    if (traduccion.getidioma().equals(selecIdioma)) {
                        txtFrasestraducciones.setText(traduccion.gettextoTraducido());
                        traduccionEncontrada=true;


                    }

                    }

                }

            }

        if (!traduccionEncontrada) {
            JOptionPane.showMessageDialog(null, "La frase seleccionada no tiene traducción");
            txtFrasestraducciones.setText("");
            return;
        }
    }

    public String formatearTexto(String texto) {
        String nuevaPalabra = "";
        for (int i = 0; i < texto.length(); i++) {
            nuevaPalabra += texto.charAt(i);
            nuevaPalabra = nuevaPalabra.replace("á", "a")
                    .replace("é", "e")
                    .replace("í", "i")
                    .replace("ó", "o")
                    .replace("ú", "u")
                    .replace("ñ", "n")
                    .replace(" ", "")
                    .replace("?", "");
            if (texto.charAt(i) == ' ') {
                i++;
                if (i + 1 < texto.length()) {
                    nuevaPalabra += Character.toUpperCase(texto.charAt(i));
                }

            }
        }
        return nuevaPalabra;
    }

    private void Play() {
        String fraseformateada = formatearTexto(selecFrase);
        String ruta = "src/Audios/" + fraseformateada + "-" + selecIdioma + ".mp3";
        System.out.println(ruta);
        ReproductorAudio.reproducir(ruta);
    }
}
