package dev.luteoos.scrumbet.android.core

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import org.koin.androidx.viewmodel.ext.android.viewModelForClass
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

abstract class BaseFragment<T : BaseViewModel, Binding : ViewBinding>(private val clazz: KClass<T>) : Fragment() {

    protected lateinit var model: T

    protected lateinit var binding: Binding

    protected abstract val layoutId: Int

    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> Binding

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = if (clazz.isSubclassOf(SharedModel::class) && activity != null) {
            activity!!.viewModelForClass(clazz).value
        } else {
            viewModelForClass(clazz).value
        }
        model.hideKeyboard.observe(this, Observer { hideKeyboard(this.view) })
        initObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindingValues()
        initFlowCollectors()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = bindingInflater(inflater, container, false)
        return binding.root
    }

    abstract fun initObservers()

    abstract fun initBindingValues()

    /**
     *  ```
     *  viewLifecycleOwner.lifecycleScope.launchWhenStarted { collect }
     *  ```
     */
    abstract fun initFlowCollectors()

    /**
     * call to hide keyboard
     */
    fun hideKeyboard(view: View?) {
        view?.let {
            (it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(view.windowToken, 0)
            it.isFocusable = false
            it.isFocusableInTouchMode = false
            it.isFocusable = true
            it.isFocusableInTouchMode = true
            it.requestFocus()
        }
    }
}
