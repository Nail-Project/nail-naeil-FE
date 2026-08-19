package com.example.nailnaeil.ui.main.admin

import androidx.lifecycle.ViewModel
import com.example.nailnaeil.data.demo.DemoAdminRequestSummary
import com.example.nailnaeil.data.demo.DemoEngine
import kotlinx.coroutines.flow.StateFlow

/**
 * 네트워크 호출 없이 DemoEngine의 인메모리 상태를 그대로 구독한다.
 * 데모 견적 요청은 앱 프로세스가 살아있는 동안만 유지된다.
 */
class AdminDemoListViewModel : ViewModel() {

    val activeRequests: StateFlow<List<DemoAdminRequestSummary>> = DemoEngine.activeRequests

    fun forceRespondNow(requestId: Long) {
        DemoEngine.forceRespondNow(requestId)
    }
}
