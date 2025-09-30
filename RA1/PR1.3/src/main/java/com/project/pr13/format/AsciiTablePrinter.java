package com.project.pr13.format;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AsciiTablePrinter {

    public static void imprimirTaula(List<String> capçaleres, List<List<String>> dades) {
        List<Integer> amplades = calcularAmpladesDeColumnes(capçaleres, dades);
        imprimirSeparador(amplades);
        imprimirFila(capçaleres, amplades);
        imprimirSeparador(amplades);
        dades.forEach(fila -> imprimirFila(fila, amplades));
        imprimirSeparador(amplades);
    }

    private static List<Integer> calcularAmpladesDeColumnes(List<String> capçaleres, List<List<String>> dades) {
        return IntStream.range(0, capçaleres.size())
                .map(i -> Math.max(
                        capçaleres.get(i).length(),
                        dades.stream().mapToInt(fila -> fila.get(i).length()).max().orElse(0)
                ) + 2) // +2 per espai abans i després del text
                .boxed()
                .collect(Collectors.toList());
    }

    private static void imprimirSeparador(List<Integer> amplades) {
        amplades.forEach(amplada -> System.out.print("+" + "-".repeat(amplada)));
        System.out.println("+");
    }

    private static void imprimirFila(List<String> fila, List<Integer> amplades) {
        IntStream.range(0, fila.size())
                .forEach(i -> System.out.printf("| %-" + (amplades.get(i) - 2) + "s ", fila.get(i))); // -2 per espai abans i després del text
        System.out.println("|");
    }
}
