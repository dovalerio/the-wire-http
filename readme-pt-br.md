# The wire Http

O que é o The Wire?
Um cliente HTTP leve, baseado em middleware, feito para Kotlin.
Chega de reconfigurar HTTP em todo projeto.
O The Wire oferece uma pipeline simples e extensível para construir clientes HTTP robustos com logging, retry e comportamentos customizados — sem frameworks pesados.
 
✨ Por que usar?
Quase todo projeto repete o mesmo ritual:
• 
Configurar HttpClient
• 
Adicionar logging
• 
Criar retry manual
• 
Tratar timeout
• 
Envelopar resposta
• 
Repetir no próximo projeto
O The Wire centraliza isso em uma pipeline limpa e composável.
Você só precisa se preocupar com:
• 
URL base
• 
Rotas
• 
Tipos de request/response
O resto vira infraestrutura reutilizável.
 
🚀 Recursos
• 
API Kotlin-first
• 
Pipeline de middlewares
• 
Usa Java 21 HttpClient
• 
Logging estruturado
• 
Suporte a retry
• 
Leve e sem framework
• 
Fácil de testar
• 
Sem reflexão ou mágica oculta
 
🎯 Filosofia
O The Wire não é framework.
Não é engine de annotation.
Não é mágica.
É:
• 
Simples
• 
Explícito
• 
Previsível
• 
Extensível
Infraestrutura deve ser entediante.
E confiável.