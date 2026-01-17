# 📋 DS Evaluation Checklist - Architecture des Composants

## Étudiant: _________________
## Date: _________________

---

## ✅ 1. Architecture Microservices avec Spring Cloud (20 points)

### Config Server (4 points)
- [ ] Config Server créé et fonctionnel (port 8888)
- [ ] Repository Git configuré (config-repo/)
- [ ] Services clients connectés au Config Server
- [ ] Configuration centralisée pour tous les services

**Points obtenus: ____ / 4**

### Eureka Server (3 points)
- [ ] Eureka Server déployé (port 8761)
- [ ] Dashboard Eureka accessible
- [ ] Tous les services enregistrés dans Eureka

**Points obtenus: ____ / 3**

### Microservices (5 points)
- [ ] Student-Service fonctionnel (port 8089)
- [ ] Department-Service fonctionnel (port 8088)
- [ ] Enrollment-Service fonctionnel (port 8090)
- [ ] Bases de données séparées par service
- [ ] Communication inter-services via Feign

**Points obtenus: ____ / 5**

### API Gateway (4 points)
- [ ] Gateway configuré (port 8080)
- [ ] Routes configurées pour tous les services
- [ ] Load balancing fonctionnel
- [ ] Point d'entrée unique pour les requêtes

**Points obtenus: ____ / 4**

### Communication (4 points)
- [ ] OpenFeign configuré et fonctionnel
- [ ] Communication synchrone REST testée
- [ ] @FeignClient implémenté correctement
- [ ] Gestion des erreurs de communication

**Points obtenus: ____ / 4**

---

## ✅ 2. Résilience avec Resilience4j (30 points)

### Configuration (5 points)
- [ ] Resilience4j ajouté aux dépendances
- [ ] Configuration dans application.yml/properties
- [ ] Patterns configurés correctement
- [ ] AOP activé (@EnableAspectJAutoProxy)

**Points obtenus: ____ / 5**

### Circuit Breaker (8 points)
- [ ] Circuit Breaker implémenté
- [ ] Configuration: sliding-window-size = 10
- [ ] Configuration: failure-rate-threshold = 50%
- [ ] Configuration: wait-duration = 10s
- [ ] Annotation @CircuitBreaker utilisée
- [ ] Test: service indisponible → circuit ouvert
- [ ] Métriques circuit breaker accessibles
- [ ] Dashboard montre l'état du circuit

**Points obtenus: ____ / 8**

### Retry (7 points)
- [ ] Retry implémenté
- [ ] Configuration: max-attempts = 3
- [ ] Configuration: wait-duration = 500ms
- [ ] Exponential backoff configuré
- [ ] Annotation @Retry utilisée
- [ ] Test: échecs transitoires réessayés
- [ ] Logs montrent les tentatives de retry

**Points obtenus: ____ / 7**

### Rate Limiter (6 points)
- [ ] Rate Limiter implémenté
- [ ] Configuration: 5 requêtes/seconde
- [ ] Annotation @RateLimiter utilisée
- [ ] Test: >5 requêtes/sec rejetées
- [ ] Métriques rate limiter accessibles
- [ ] Message d'erreur approprié (429 Too Many Requests)

**Points obtenus: ____ / 6**

### Fallback Methods (4 points)
- [ ] Méthode fallback implémentée
- [ ] Signature correcte (même paramètres + Throwable)
- [ ] Logique de secours appropriée
- [ ] Test: fallback exécuté lors d'échec

**Points obtenus: ____ / 4**

---

## ✅ 3. Surveillance et Monitoring (20 points)

### Micrometer (4 points)
- [ ] Micrometer configuré dans tous les services
- [ ] Métriques exposées via actuator
- [ ] Annotations de tracing présentes
- [ ] Métriques personnalisées créées

**Points obtenus: ____ / 4**

### Prometheus (5 points)
- [ ] Prometheus déployé (port 9090)
- [ ] Configuration prometheus.yml créée
- [ ] Tous les services scrapés
- [ ] Métriques visibles dans Prometheus UI
- [ ] Requêtes PromQL fonctionnelles

**Points obtenus: ____ / 5**

### Grafana (5 points)
- [ ] Grafana déployé (port 3000)
- [ ] Datasource Prometheus configurée
- [ ] Dashboard(s) créé(s)
- [ ] Visualisations des métriques
- [ ] Alertes configurées (bonus)

**Points obtenus: ____ / 5**

### Zipkin (6 points)
- [ ] Zipkin déployé (port 9411)
- [ ] Tracing configuré dans tous les services
- [ ] Traces visibles dans Zipkin UI
- [ ] Trace ID propagé entre services
- [ ] Latence visible par service
- [ ] Dépendances visualisées

**Points obtenus: ____ / 6**

---

## ✅ 4. Load Balancing (10 points)

### Configuration (4 points)
- [ ] Spring Cloud LoadBalancer configuré
- [ ] Eureka intégré pour discovery
- [ ] Ribbon/LoadBalancer configuration
- [ ] prefer-ip-address activé

**Points obtenus: ____ / 4**

### Tests (6 points)
- [ ] Multiple instances d'un service lancées
- [ ] Requêtes distribuées entre instances
- [ ] Logs montrent l'équilibrage
- [ ] Round-robin vérifié
- [ ] Failover testé (instance down)
- [ ] Métriques de distribution visibles

**Points obtenus: ____ / 6**

---

## ✅ 5. CQRS & Axon Framework (20 points)

### Configuration Axon (4 points)
- [ ] Axon Framework ajouté aux dépendances
- [ ] Axon Server déployé (ports 8024, 8124)
- [ ] Configuration axon dans application.yml
- [ ] Connection à Axon Server vérifiée

**Points obtenus: ____ / 4**

### Commands (3 points)
- [ ] Commands créées (CreateEnrollment, UpdateStatus)
- [ ] @TargetAggregateIdentifier présent
- [ ] Validation dans commands
- [ ] CommandHandler implémenté

**Points obtenus: ____ / 3**

### Events (3 points)
- [ ] Events créées (EnrollmentCreated, StatusUpdated)
- [ ] Events correctement structurées
- [ ] EventSourcingHandler implémenté
- [ ] Events visibles dans Axon Server

**Points obtenus: ____ / 3**

### Aggregate (4 points)
- [ ] Aggregate créé avec @Aggregate
- [ ] @AggregateIdentifier présent
- [ ] CommandHandler dans aggregate
- [ ] EventSourcingHandler dans aggregate
- [ ] Logique métier dans aggregate

**Points obtenus: ____ / 4**

### Projections & Queries (4 points)
- [ ] Projection créée avec @EventHandler
- [ ] Read model (entité JPA) créé
- [ ] QueryHandler implémenté
- [ ] Queries créées et testées

**Points obtenus: ____ / 4**

### API REST (2 points)
- [ ] Endpoints Command (POST, PUT)
- [ ] Endpoints Query (GET)
- [ ] CommandGateway utilisé
- [ ] QueryGateway utilisé

**Points obtenus: ____ / 2**

---

## ✅ 6. Gestion des Types de Défaillances (Bonus: 10 points)

### Défaillance de disponibilité (2 points)
- [ ] Service arrêté
- [ ] Circuit breaker gère l'indisponibilité
- [ ] Fallback exécuté

**Points obtenus: ____ / 2**

### Défaillance de temps de réponse (2 points)
- [ ] Timeout simulé
- [ ] Retry avec timeout
- [ ] Fallback après timeout

**Points obtenus: ____ / 2**

### Réponses incorrectes (2 points)
- [ ] Données corrompues simulées
- [ ] Validation et gestion d'erreur
- [ ] Fallback avec données par défaut

**Points obtenus: ____ / 2**

### Erreurs réseau (2 points)
- [ ] URL incorrecte simulée
- [ ] Exception réseau capturée
- [ ] Retry puis fallback

**Points obtenus: ____ / 2**

### Défaillances dépendances externes (2 points)
- [ ] Base de données inaccessible
- [ ] Circuit breaker ouvert
- [ ] Service dégradé fonctionnel

**Points obtenus: ____ / 2**

---

## 📊 Récapitulatif des Points

| Catégorie | Points Max | Points Obtenus |
|-----------|------------|----------------|
| 1. Architecture Microservices | 20 | ____ |
| 2. Résilience (Resilience4j) | 30 | ____ |
| 3. Surveillance (Monitoring) | 20 | ____ |
| 4. Load Balancing | 10 | ____ |
| 5. CQRS & Axon | 20 | ____ |
| **TOTAL** | **100** | **____** |
| 6. Bonus (Défaillances) | 10 | ____ |
| **TOTAL AVEC BONUS** | **110** | **____** |

---

## 📝 Commentaires de l'Évaluateur

### Points forts:
```
_________________________________________________________________
_________________________________________________________________
_________________________________________________________________
```

### Points à améliorer:
```
_________________________________________________________________
_________________________________________________________________
_________________________________________________________________
```

### Remarques générales:
```
_________________________________________________________________
_________________________________________________________________
_________________________________________________________________
```

---

## ✍️ Signatures

**Étudiant:** ___________________ **Date:** ___________

**Évaluateur:** _________________ **Date:** ___________

---

**Matière:** Architecture des Composants  
**Section:** GL5  
**A.U:** 2024-2025  
**Enseignant:** Heithem Abbes  
**Enseignante TPs:** Thouraya LOUATI
