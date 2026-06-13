package com.carsharing.app.data

import com.carsharing.app.models.ServiceWork
import com.carsharing.app.models.WorkCategory

object ServiceWorksData {
    
    val allWorks = listOf(
        ServiceWork("work_1", "Замена масла в двигателе", 35, WorkCategory.ENGINE, 2500),
        ServiceWork("work_2", "Замена масляного фильтра", 15, WorkCategory.ENGINE, 800),
        ServiceWork("work_3", "Замена воздушного фильтра", 10, WorkCategory.FILTERS, 500),
        ServiceWork("work_4", "Замена салонного фильтра", 10, WorkCategory.FILTERS, 600),
        ServiceWork("work_5", "Замена передних тормозных колодок", 60, WorkCategory.BRAKES, 1800),
        ServiceWork("work_6", "Замена задних тормозных колодок", 50, WorkCategory.BRAKES, 1700),
        ServiceWork("work_7", "Замена тормозных дисков (передние)", 90, WorkCategory.BRAKES, 3000),
        ServiceWork("work_8", "Развал-схождение", 45, WorkCategory.SUSPENSION, 1500),
        ServiceWork("work_9", "Компьютерная диагностика", 40, WorkCategory.ELECTRICAL, 1000),
        ServiceWork("work_10", "Шиномонтаж (1 колесо)", 20, WorkCategory.TIRES, 500)
    )
    
    fun getWorksByCategory(): Map<WorkCategory, List<ServiceWork>> {
        return allWorks.groupBy { it.category }
    }
    
    fun getTotalDuration(workIds: List<String>): Int {
        return workIds.sumOf { workId ->
            allWorks.find { it.id == workId }?.durationMinutes ?: 0
        }
    }
}
