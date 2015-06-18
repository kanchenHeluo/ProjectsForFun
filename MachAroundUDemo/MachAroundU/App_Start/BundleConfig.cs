using System.Web;
using System.Web.Optimization;

namespace MachAroundU
{
    public class BundleConfig
    {
        // For more information on Bundling, visit http://go.microsoft.com/fwlink/?LinkId=254725
        public static void RegisterBundles(BundleCollection bundles)
        {
            bundles.Add(new ScriptBundle("~/bundles/jquery").Include(
                        "~/Scripts/jquery-{version}.js"));

            bundles.Add(new ScriptBundle("~/bundles/jqueryui").Include(
                        "~/Scripts/jquery-ui-{version}.js"));

            bundles.Add(new ScriptBundle("~/bundles/angular").Include(
                        "~/Scripts/angular.js",
                        "~/Scripts/angular-ui-router.js",
                        "~/Scripts/angula-animate.js"));

            bundles.Add(new ScriptBundle("~/bundles/bootstrap").Include(
                      "~/Scripts/bootstrap.js",
                      "~/Scripts/respond.js",
                      "~/Scripts/ui-bootstrap-tpls-0.12.0.js"));

            bundles.Add(new ScriptBundle("~/common/js").Include(
                        "~/Scripts/Common/commonApp.js",
                        "~/Scripts/Global/MessageBanner.js",
                        "~/Scripts/Global/Modal_dialog.js",
                        "~/Scripts/Common/Services/notificationSvc.js",
                        "~/Scripts/Common/Services/ajaxSvc.js",
                        "~/Scripts/Common/Directives/ckForm.js",
                        "~/Scripts/Common/Directives/ckNumber.js",
                        "~/Scripts/Common/Directives/ckRequired.js",
                        "~/Scripts/Common/Directives/ckErrorSummary.js",
                        "~/Scripts/Common/Directives/ckDatepicker.js",
                        "~/Scripts/Common/Directives/opDialog.js",
                        "~/Scripts/Common/Directives/ckPagination.js",
                        "~/Scripts/Common/Directives/ckHourMin.js"));

            bundles.Add(new ScriptBundle("~/app/js").Include(
                        "~/Scripts/App/app.js",
                        "~/Scripts/App/controller/*.js",
                        "~/Scripts/App/Services/*.js"));

            bundles.Add(new ScriptBundle("~/bundles/jqueryval").Include(
                        "~/Scripts/jquery.unobtrusive*",
                        "~/Scripts/jquery.validate*"));

            // Use the development version of Modernizr to develop with and learn from. Then, when you're
            // ready for production, use the build tool at http://modernizr.com to pick only the tests you need.
            bundles.Add(new ScriptBundle("~/bundles/modernizr").Include(
                        "~/Scripts/modernizr-*"));

            
            bundles.Add(new StyleBundle("~/Content/themes/base/css").Include(
                        "~/Content/themes/base/jquery.ui.core.css",
                        "~/Content/themes/base/jquery.ui.resizable.css",
                        "~/Content/themes/base/jquery.ui.selectable.css",
                        "~/Content/themes/base/jquery.ui.accordion.css",
                        "~/Content/themes/base/jquery.ui.autocomplete.css",
                        "~/Content/themes/base/jquery.ui.button.css",
                        "~/Content/themes/base/jquery.ui.dialog.css",
                        "~/Content/themes/base/jquery.ui.slider.css",
                        "~/Content/themes/base/jquery.ui.tabs.css",
                        "~/Content/themes/base/jquery.ui.datepicker.css",
                        "~/Content/themes/base/jquery.ui.progressbar.css",
                        "~/Content/themes/base/jquery.ui.theme.css"));


            bundles.Add(new StyleBundle("~/Content/css").Include(
                "~/Content/bootstrap.css",
                "~/Content/Global/MessageBanner.css",
                "~/Content/Global/Modal_dialog.css",
                "~/Content/site.css",
                "~/Content/App/topic.css",
                "~/Content/Common/validation.css",
                "~/Content/Common/ckErrorSummary.css"));

            // Set EnableOptimizations to false for debugging. For more information,
            BundleTable.EnableOptimizations = false;
        }
    }
}