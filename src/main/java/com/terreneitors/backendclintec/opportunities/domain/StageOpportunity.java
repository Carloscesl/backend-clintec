package com.terreneitors.backendclintec.opportunities.domain;

public enum StageOpportunity {
    PROSPECCION(5,   20,  10),
    CALIFICACION(21,  40,  30),
    PROPUESTA      (41,  60,  50),
    NEGOCIACION (61,  85,  75),
    CIERRE_GANADO  (100, 100, 100),
    CIERRE_PERDIDO (0,   0,   0);

    private final int min;
    private final int max;
    private final int probabilidadDefault;

    StageOpportunity(int min, int max, int probabilidadDefault) {
        this.min                 = min;
        this.max                 = max;
        this.probabilidadDefault = probabilidadDefault;
    }

    public int getMin()                 { return min; }
    public int getMax()                 { return max; }
    public int getProbabilidadDefault() { return probabilidadDefault; }

}
