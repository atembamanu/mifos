package org.mifos.mobilebanking.ui.fragments;

/*
~This project is licensed under the open source MPL V2.
~See https://github.com/openMF/self-service-app/blob/master/LICENSE.md
*/

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.models.accounts.loan.LoanWithAssociations;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;
import org.mifos.mobilebanking.utils.Constants;
import org.mifos.mobilebanking.utils.CurrencyUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dilpreet on 25/2/17.
 */

public class LoanAccountSummaryFragment extends BaseFragment {

    @BindView(R.id.tv_loan_product_name)
    TextView tvLoanProductName;

    @BindView(R.id.tv_principal)
    TextView tvPrincipalName;

    @BindView(R.id.tv_interest_charged)
    TextView tvInterestChargedName;

    @BindView(R.id.tv_fees)
    TextView tvFeesName;

    @BindView(R.id.tv_penalties)
    TextView tvPenaltiesName;

    @BindView(R.id.tv_total_repayment)
    TextView tvTotalRepaymentName;

    @BindView(R.id.tv_total_paid)
    TextView tvTotalPaidName;

    @BindView(R.id.tv_interest_waived)
    TextView tvInterestWaivedName;

    @BindView(R.id.tv_penalties_waived)
    TextView tvPenaltiesWaivedName;

    @BindView(R.id.tv_fees_waived)
    TextView tvFeesWaivedName;

    @BindView(R.id.tv_outstanding_balance)
    TextView tvOutstandingBalanceName;

    @BindView(R.id.ll_error)
    View layoutError;

    @BindView(R.id.tv_account_status)
    TextView tvAccountStatus;

    @BindView(R.id.tv_status)
    TextView tvStatus;

    @BindView(R.id.ll_loan_summary)
    LinearLayout llLoanSummary;

    @BindView(R.id.ll_loan_purpose)
    LinearLayout llLoanPurpose;

    @BindView(R.id.tv_loan_account_number)
    TextView tvLoanAccountNumber;

    @BindView(R.id.tv_loan_purpose)
    TextView tvLoanPurpose;

    @BindView(R.id.iv_account_status)
    ImageView ivAccountStatus;

    private LoanWithAssociations loanWithAssociations;

    private View rootView;

    public static LoanAccountSummaryFragment
            newInstance(LoanWithAssociations loanWithAssociations) {
        LoanAccountSummaryFragment loanAccountSummaryFragment =
                new LoanAccountSummaryFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.LOAN_ACCOUNT, loanWithAssociations);
        loanAccountSummaryFragment.setArguments(args);
        return loanAccountSummaryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        if (getArguments() != null) {
            loanWithAssociations = getArguments().getParcelable(Constants.LOAN_ACCOUNT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_loan_account_summary, container, false);
        setToolbarTitle(getString(R.string.loan_summary));

        ButterKnife.bind(this, rootView);

        showLoanAccountsDetail(loanWithAssociations);

        return rootView;
    }

    /**
     * Sets basic information about a Loan Account
     * @param loanWithAssociations object containing details of each loan account,
     */
    public void showLoanAccountsDetail(LoanWithAssociations loanWithAssociations) {
        llLoanSummary.setVisibility(View.VISIBLE);
        System.out.println("trouble"+loanWithAssociations.getAccountNo());
        tvLoanProductName.setText(loanWithAssociations.getLoanProductName());
        tvPrincipalName.setText(CurrencyUtil.formatCurrency(getActivity(),
                loanWithAssociations.getPrincipal()));
        tvInterestChargedName.setText(CurrencyUtil.formatCurrency(getActivity(),
                loanWithAssociations.getSummary().getInterestCharged()));
        tvFeesName.setText(CurrencyUtil.formatCurrency(getActivity(),
                loanWithAssociations.getSummary().getFeeChargesCharged()));
        tvPenaltiesName.setText(CurrencyUtil.formatCurrency(getActivity(),
                loanWithAssociations.getSummary().getPenaltyChargesCharged()));
        tvTotalRepaymentName.setText(CurrencyUtil.formatCurrency(getActivity(),
                loanWithAssociations.getSummary().getTotalExpectedRepayment()));
        tvTotalPaidName.setText(CurrencyUtil.formatCurrency(getActivity(),
                loanWithAssociations.getSummary().getTotalRepayment()));
        tvInterestWaivedName.setText(CurrencyUtil.formatCurrency(getActivity(),
                loanWithAssociations.getSummary().getInterestWaived()));
        tvPenaltiesWaivedName.setText(CurrencyUtil.formatCurrency(getActivity(),
                loanWithAssociations.getSummary().getPenaltyChargesWaived()));
        tvFeesWaivedName.setText(CurrencyUtil.formatCurrency(getActivity(),
                loanWithAssociations.getSummary().getFeeChargesWaived()));
        tvOutstandingBalanceName.setText(getResources().getString(R.string.string_and_string,
                loanWithAssociations.getSummary().getCurrency().getDisplaySymbol(), CurrencyUtil.
                formatCurrency(getActivity(),
                        loanWithAssociations.getSummary().getTotalOutstanding())));
        tvLoanAccountNumber.setText(loanWithAssociations.getAccountNo());
        if (loanWithAssociations.getLoanPurposeName() != null) {
            llLoanPurpose.setVisibility(View.VISIBLE);
            tvLoanPurpose.setText(loanWithAssociations.getLoanPurposeName());
        }
        if (loanWithAssociations.getStatus().getActive()) {
            tvAccountStatus.setText(R.string.active_uc);
            ivAccountStatus.setImageResource(R.drawable.ic_check_circle_green_24px
            );
        } else {
            tvAccountStatus.setText(R.string.inactive_uc);
            ivAccountStatus.setImageResource(R.drawable.ic_report_problem_red_24px);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideProgressBar();
    }


}
