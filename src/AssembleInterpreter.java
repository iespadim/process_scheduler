public class AssembleInterpreter {
    //instrucoes

    /**
     Categoria          Mnemônico                                 Função
     Aritmético         ADD  op1                                   acc=acc+(op1)
     Aritmético         SUB  op1                                   acc=acc–(op1)
     Aritmético         MULT op1                                   acc=acc*(op1)
     Aritmético         DIV  op1                                   acc=acc/(op1)
     Memória            LOAD op1                                   acc=(op1)
     Memória            STORE op1                                  (op1)=acc
     Salto              BRANY label                                pc <- label
     Salto              BRPOS label                                Se acc > 0 então pc <- op1
     Salto              BRZERO label                               Se acc = 0 então pc <- op1
     Salto              BRNEG label                                Se acc < 0 então pc <- op1
     Sistema            SYSCALL index                              Chamada de sistema
     */
    public AssembleInterpreter() {

    }
}
