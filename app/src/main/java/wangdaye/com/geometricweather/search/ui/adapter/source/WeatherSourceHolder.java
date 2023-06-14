package nowsci.com.temperateweather.search.ui.adapter.source;

import androidx.recyclerview.widget.RecyclerView;

import nowsci.com.temperateweather.databinding.ItemWeatherSourceBinding;

class WeatherSourceHolder extends RecyclerView.ViewHolder {

    private final ItemWeatherSourceBinding mBinding;

    WeatherSourceHolder(ItemWeatherSourceBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

    void onBind(WeatherSourceModel model) {
        mBinding.checkbox.setChecked(model.isEnabled());
        mBinding.title.setText(model.getSource().getName(itemView.getContext()));

        mBinding.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            model.setEnabled(isChecked);
            mBinding.checkbox.setChecked(model.isEnabled());
        });
        mBinding.container.setOnClickListener(v -> {
            model.setEnabled(!model.isEnabled());
            mBinding.checkbox.setChecked(model.isEnabled());
        });
    }
}
