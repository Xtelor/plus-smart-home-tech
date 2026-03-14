package ru.yandex.practicum.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.analyzer.model.ScenarioAction;

import java.util.List;

public interface ScenarioActionRepository extends JpaRepository<ScenarioAction, Long> {
    List<ScenarioAction> findByScenarioId(Long scenarioId);
    void deleteByScenarioId(Long scenarioId);
}
