import {Pie, PieChart} from 'recharts';
import {RechartsDevtools} from '@recharts/devtools';
// #region Sample data
// const data = [{name: 'Group A', value: 400}, {name: 'Group B', value: 300}, {
//     name: 'Group C',
//     value: 300
// }, {name: 'Group D', value: 200}];
interface HabitStat {
    name: string;
    value: number;
}
export default function PieChartWithPaddingAngle({isAnimationActive = true,  data}: {
    isAnimationActive?: boolean,
    data?: HabitStat[]
}) {
    return (
        <PieChart style={{width: '100%', maxWidth: '500px', maxHeight: '80vh', aspectRatio: 1}} responsive>
            <Pie
                data={data}
                innerRadius="80%"
                outerRadius="100%"
                // Corner radius is the rounded edge of each pie slice\n
                cornerRadius="50%"    // padding angle is the gap between each pie slice\n
                paddingAngle={5}
                dataKey="value"
                isAnimationActive={isAnimationActive}/>
            <RechartsDevtools/>
        </PieChart>);
}